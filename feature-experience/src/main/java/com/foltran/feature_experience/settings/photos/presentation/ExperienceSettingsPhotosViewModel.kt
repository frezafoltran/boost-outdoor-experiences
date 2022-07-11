package com.foltran.feature_experience.settings.photos.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.foltran.core_charts.adapters.ChartCallback
import com.foltran.core_charts.adapters.MessageModel
import com.foltran.core_charts.models.ChartDataModelPhotos
import com.foltran.core_map.webview.Photo
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.bases.BaseViewModel
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.core.domain.model.photoIdByLatLng
import com.foltran.feature_experience.core.domain.model.photoIdByUrl
import com.foltran.feature_experience.core.domain.model.toWebViewPhotoMapById
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

sealed class ExperienceSettingsPhotoState {
    object AddPhoto : ExperienceSettingsPhotoState()
    data class RemovePhoto(val photo: Photo) : ExperienceSettingsPhotoState()
    object FinishActivity : ExperienceSettingsPhotoState()
}

class ExperienceSettingsPhotosViewModel(
    val useCase: ExperienceSettingsPhotoUseCase,
    initialExperience: Experience
) : BaseViewModel(), ChartCallback {

    private val _state = MutableLiveData<ExperienceSettingsPhotoState>()
    val state: LiveData<ExperienceSettingsPhotoState> = _state

    private val _experience = MutableLiveData(initialExperience)
    val experience: LiveData<Experience> = _experience

    val chart = MutableLiveData<ChartDataModelPhotos>()
    var chartCurrentIndexSelected = MutableLiveData<Int>()

    private val coordinateStream = MutableLiveData<List<List<Double>>>()

    val currentLat = MutableLiveData<Double>()
    val currentLng = MutableLiveData<Double>()

    val currentPhotos = MutableLiveData(_experience.value?.photos.toWebViewPhotoMapById())

    val currentPhotoUrl = MutableLiveData<String>()
    val isPhotoSelected: LiveData<Boolean> = Transformations.map(currentPhotoUrl) {
        currentPhotoUrl.value.isNullOrEmpty().not()
    }

    val hasPendingPhotosChange = MutableLiveData<Boolean>()

    val valsIndexToHighlight = MediatorLiveData<List<Pair<Double, List<Double>>>>().apply {
        addSource(currentPhotos) {
            coordinateStream.value?.let { stream ->
                value = getStreamIndexWithPhotos(it, stream)
            }
        }
        addSource(coordinateStream) {
            currentPhotos.value?.let { map ->
                value = getStreamIndexWithPhotos(map, it)
            }
        }
    }

    val chartSelectorRatio = MutableLiveData<Float>()

    val currentDistanceLabel = Transformations.map(chartCurrentIndexSelected) {
        chart.value?.vals?.get(it)?.meterToKm()
    }

    private val currentPhotosObserver = Observer<Map<String, Photo>> {
        hasPendingPhotosChange.value = true
    }

    private val experienceObserver = Observer<Experience> {
        currentPhotos.value = _experience.value?.photos.toWebViewPhotoMapById()
    }

    private val currentLatObserver = Observer<Double> {
        selectPhoto(it, currentLng.value)
    }

    private val currentLonObserver = Observer<Double> {
        selectPhoto(currentLat.value, it)
    }

    private val chartCurrentIndexSelectedObserver = Observer<Int> {
        coordinateStream.value?.let { stream ->
            currentLat.postValue(stream[it][0])
            currentLng.postValue(stream[it][1])
        }
        chart.value?.vals?.size?.let { total ->
            if (total != 0) chartSelectorRatio.value = it.toFloat() / total.toFloat()
        }
    }

    init {
        getExperienceStream(_experience.value!!.id)

        _experience.observeForever(experienceObserver)
        currentPhotos.observeForever(currentPhotosObserver)
        currentLat.observeForever(currentLatObserver)
        currentLng.observeForever(currentLonObserver)
        chartCurrentIndexSelected.observeForever(chartCurrentIndexSelectedObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        _experience.removeObserver(experienceObserver)
        currentPhotos.removeObserver(currentPhotosObserver)
        currentLat.removeObserver(currentLatObserver)
        currentLng.removeObserver(currentLonObserver)
        chartCurrentIndexSelected.removeObserver(chartCurrentIndexSelectedObserver)
    }

    /**
     * Output is Pair<Double, List<Double>> = Pair<ratio, {lat, lon}>
     */
    private fun getStreamIndexWithPhotos(
        currentPhotos: Map<String, Photo>,
        coordinateStream: List<List<Double>>
    ) = mutableListOf<Pair<Double, List<Double>>>().apply {

        val maxIndex = coordinateStream.size.toDouble()
        currentPhotos.forEach { entry ->
            val latLngPair = listOf(entry.value.lat, entry.value.lon)
            val indexOfMatch = coordinateStream.indexOf(latLngPair)
            if (indexOfMatch != -1) {
                add(Pair(indexOfMatch.toDouble() / maxIndex, latLngPair))
            }
        }
    }

    private fun getExperienceStream(experienceId: String) {
        useCase.getExperienceStream(experienceId).onEach { out ->
            when (out) {
                is Resource.Success -> {

                    out.data?.let {
                        chart.value = ChartDataModelPhotos(
                            vals = it.distance
                        )
                        coordinateStream.value = it.latLng
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun finishActivity() {
        _state.value = ExperienceSettingsPhotoState.FinishActivity
    }

    fun addPhoto() {
        _state.value = ExperienceSettingsPhotoState.AddPhoto
    }

    fun selectPhoto(lat: Double?, lon: Double?) {

        val photo = currentPhotos.value?.let {
            it[it.photoIdByLatLng(lat, lon)]
        }

        photo?.let {
            val coordinateIndex = coordinateStream.value?.indexOf(listOf(lat, lon))
            if (coordinateIndex != null && coordinateIndex != -1 && chartCurrentIndexSelected.value != coordinateIndex) {
                chartCurrentIndexSelected.value = coordinateIndex!!
                return
            }

            if (it.url.isNullOrEmpty().not() && currentPhotoUrl.value == it.url) return

            it.url?.let { url ->
                currentPhotoUrl.value = url
            } ?: run {
                useCase.getExperiencePhoto(
                    photoId = it.id
                ).onEach { out ->
                    when (out) {
                        is Resource.Success -> {
                            out.data?.photoUrl?.let { url ->
                                currentPhotoUrl.value = url
                                currentPhotos.value!![photo.id]!!.url = url
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        } ?: run {
            currentPhotoUrl.value = ""
        }
    }

    fun removePhotoCommit() {
        if (isPhotoSelected.value == false) return

        currentPhotos.value.photoIdByUrl(currentPhotoUrl.value)?.let { photoId ->
            useCase.removeExperiencePhoto(
                photoId = photoId,
                experienceId = _experience.value!!.id
            ).onEach { out ->
                when (out) {
                    is Resource.Success -> {
                        out.data?.let {
                            _experience.value = it.updatedExperience
                        }
                        currentPhotoUrl.value = ""
                    }
                    is Resource.Error -> {
                        Log.i("JVFF", out.message ?: "no error message found")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun distanceBetweenPoints(a: List<Double>, b: List<Double>): Double {
        return sqrt(abs(a[0] - b[0]).pow(2.0) + abs(a[1] - b[1]).pow(2.0))
    }

    fun addPhotoCommit(base64: String) {

        if (
            currentLat.value == null ||
            currentLng.value == null ||
            chartCurrentIndexSelected.value == null
        ) return

        var indexInCoordinateStream = chartCurrentIndexSelected.value!!
//        var curMinDistance = Double.MAX_VALUE
//
//        coordinateStream.value?.forEachIndexed { i, item ->
//            val d = distanceBetweenPoints(item, listOf(currentLat.value!!, currentLng.value!!))
//            if (d < curMinDistance) {
//                curMinDistance = d
//                indexInCoordinateStream = i
//            }
//        }

        useCase.putExperiencePhoto(
            base64 = base64,
            photoLat = currentLat.value!!,
            photoLon = currentLng.value!!,
            experienceId = _experience.value!!.id,
            indexInStream = indexInCoordinateStream
        ).onEach { out ->
            when (out) {
                is Resource.Success -> {
                    out.data?.let {
                        _experience.value = it.updatedExperience
                    }
                    selectPhoto(currentLat.value!!, currentLng.value!!)
                }
                is Resource.Error -> {
                    Log.i("JVFF", out.message ?: "no error message found")
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun chartViewMoveOverEventMessage(
        messageModel: MessageModel
    ) {

        messageModel.x?.toInt()?.let {
            Handler(Looper.getMainLooper()).post {
                chartCurrentIndexSelected.postValue(it)
            }
        }
//        val curIndex = messageModel.x?.toInt()
//
//        if (curIndex != null) {
//
//            val mainHandler = Handler(Looper.getMainLooper())
//            mainHandler.post {
//                chartCurrentIndexSelected.postValue(curIndex)
////                coordinateStream.value?.let {
////                    currentLat.postValue(it[curIndex][0])
////                    currentLng.postValue(it[curIndex][1])
////                }
//            }
//        }
    }
}