package com.foltran.feature_experience.core.presentation

import android.util.Log
import androidx.lifecycle.*
import com.foltran.core_charts.models.ChartTypes
import com.foltran.core_experience.shared.data.model.*
import com.foltran.core_map.R
import com.foltran.core_map.core.models.icon.MapIcon
import com.foltran.core_map.core.models.icon.toMapIconByIndexInStream
import com.foltran.core_map.core.models.toCoordinateModel
import com.foltran.core_map.core.presentation.model.MapCoreMainView
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_map.experience.player.ExperiencePlayer
import com.foltran.core_map.experience.player.ExperiencePlayerStaticData
import com.foltran.core_map.experience.player.ExperiencePlayerStatuses
import com.foltran.core_map.webview.ExperienceHighlight
import com.foltran.core_map.webview.getRandomExperienceHighlights
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.bases.BaseViewModel
import com.foltran.core_utils.formatting.meter
import com.foltran.core_utils.formatting.meterPerSecondToMinPerKmPace
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.core_utils.observable.ActionLiveData
import com.foltran.feature_experience.core.data.maps.updateExperiencePolylineParams
import com.foltran.feature_experience.core.data.maps.updateFocusCoordinates
import com.foltran.feature_experience.core.data.maps.updateStartLatLonParams
import com.foltran.feature_experience.core.domain.ExperienceUseCase
import com.foltran.feature_experience.core.domain.model.toMapExperienceHighlightSet
import com.foltran.feature_experience.core.domain.model.toMapIcons
import com.foltran.feature_experience.core.domain.model.toPathHighlightSet
import com.foltran.feature_experience.core.presentation.model.ExperienceUIModel
import com.foltran.feature_experience.core.presentation.model.toUIModel
import com.foltran.feature_experience.details.presentation.model.ExperienceDetailsChartSpinnerUIModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

sealed class ExperienceMainState {
    object Loading : ExperienceMainState()
    data class Error(val error: String?) : ExperienceMainState()
    object Success : ExperienceMainState()
    object ExpandChart : ExperienceMainState()
    object OpenShareTray : ExperienceMainState()
    data class GoToSettingsActivity(val experience: Experience) : ExperienceMainState()
    data class ToggleSettingsDrawer(val close: Boolean) : ExperienceMainState()
    object ExpandBottomSheet : ExperienceMainState()
}

data class ZoomedImageEvent(
    val imageUrl: String,
    val onZoomedImageCloseCallback: (() -> Unit)? = {},
    val closeOnTimer: Int? = null,
    val shouldCloseOnClick: Boolean = true
)

data class VideoEvent(
    val callback: () -> Unit
)

class ExperienceViewModel(
    private val useCase: ExperienceUseCase,
    val mapUIModel: ExperienceMapUIModel,
    val screenUIModel: ExperienceScreenUIModel,
    experienceFromParam: Experience
) : BaseViewModel() {

    private val _state = MutableLiveData<ExperienceMainState>()
    val state: LiveData<ExperienceMainState> = _state

    private val _experience = MutableLiveData(experienceFromParam)
    val experience: LiveData<Experience?> = _experience

    val experienceUI: LiveData<ExperienceUIModel> = Transformations.map(_experience){
        it.toUIModel()
    }

    private val _mapObservable = MutableLiveData<MapCoreUIModel>()
    val mapObservable: LiveData<MapCoreUIModel> = _mapObservable

    private val _mapCoreMainViewObservable = MutableLiveData(MapCoreMainView.MAP)
    val mapCoreMainViewObservable: LiveData<MapCoreMainView> = _mapCoreMainViewObservable

    val chartSpinnerUI = ExperienceDetailsChartSpinnerUIModel(
        onClickExpandGraph = { _state.value = ExperienceMainState.ExpandChart },
        spinnerCategories = screenUIModel.experienceChartSpinnerOptions
    )
    val chartSpinnerComparisonUI = ExperienceDetailsChartSpinnerUIModel(
        onClickExpandGraph = { _state.value = ExperienceMainState.ExpandChart },
        spinnerCategories = screenUIModel.experienceComparisonChartSpinnerOptions,
        chartType = ChartTypes.COLUMN
    )

    val experiencePlayerPlayIcon = MutableLiveData(R.drawable.ic_play)

    val experiencePlayerShouldShowFinishButton = MutableLiveData(false)

    val experiencePlayerDistance = MutableLiveData("")

    val experiencePlayerSpeed = MutableLiveData("")

    val experiencePlayerElevation = MutableLiveData("")

    private val onPhotoEvent = { photoId: String, callback: () -> Unit ->

        getPhotoUrl(photoId) { photoUrl ->
            zoomedImageEvent.postValue(
                ZoomedImageEvent(
                    imageUrl = photoUrl,
                    onZoomedImageCloseCallback = {
                        callback.invoke()
                    },
                    closeOnTimer = 3000,
                    shouldCloseOnClick = false
                )
            )

        }
    }

    private val onVideoEvent = { videoId: String, callback: () -> Unit ->
        videoEvent.postValue(VideoEvent {
            callback.invoke()
        })
    }

    val experiencePlayer = ExperiencePlayer(
        duration = EXPERIENCE_PLAYER_DURATION,
        onPhotoEvent = onPhotoEvent,
        onVideoEvent = onVideoEvent
    )

    private val experiencePlayerDistanceObserver = Observer<Double> {
        experiencePlayerDistance.value = it.meterToKm()
    }

    private val experiencePlayerElevationObserver = Observer<Double> {
        experiencePlayerElevation.value = it.meter()
    }

    private val experiencePlayerSpeedObserver = Observer<Double> {
        experiencePlayerSpeed.value = it.meterPerSecondToMinPerKmPace()
    }

    private val experiencePlayerPlayStatusObserver = Observer<ExperiencePlayerStatuses> {

        experiencePlayerPlayIcon.value =
            when (it){
                ExperiencePlayerStatuses.PLAYING -> R.drawable.ic_pause
                else -> R.drawable.ic_play
            }

        experiencePlayerShouldShowFinishButton.value = when (it){
            ExperiencePlayerStatuses.PLAYING, ExperiencePlayerStatuses.PAUSED -> true
            else -> false
        }

        _mapCoreMainViewObservable.value = when (it) {
            ExperiencePlayerStatuses.PLAYING, ExperiencePlayerStatuses.PAUSED -> MapCoreMainView.WEB_VIEW
            ExperiencePlayerStatuses.FINISHED -> MapCoreMainView.MAP
            else -> _mapCoreMainViewObservable.value
        }
    }

    //TODO highlights will come from param based on selection from ExperienceMenuFragment
    val experiencePlayerStaticData = ExperiencePlayerStaticData(
        experienceHighlightSet = getSampleExperienceHighlightSet(experienceFromParam.id).toPathHighlightSet(),
        photosByIndexInStream = experienceFromParam.photos?.toMapIcons().toMapIconByIndexInStream()
    )

    lateinit var experienceHighlights: List<ExperienceHighlight>
    val experienceHighlightsAddToMapEvent = ActionLiveData<ExperienceHighlightSet>()

    // these expire after 10 min
    private val photoUrlsById = HashMap<String, String>()

    val zoomedImageEvent = ActionLiveData<ZoomedImageEvent>()

    val videoEvent = ActionLiveData<VideoEvent>()

    val loadWebViewEvent  = ActionLiveData<Unit>()

    init {
        getExperienceData()
        syncPhotoUrls()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearObservers()
    }

    fun togglePlayStatus() {
        if(experiencePlayer.status.value == ExperiencePlayerStatuses.PLAYING) {
            experiencePlayer.pause()
        }
        else {
            experiencePlayer.start()
        }
    }

    private fun syncPhotoUrls() {
        _experience.value?.photos?.forEach {
            getPhotoUrl(it.id) {}
        }
    }

    private fun getExperienceData() {

        _experience.value?.let {
            it.map.polyline?.let { polyline ->
                mapUIModel.setMapDisplayConfigObservableMainPolyline(polyline)
            }
            _mapObservable.value =
                _mapObservable.value.updateExperiencePolylineParams(experience = it)

            getExperienceStream(it.id)
        }
    }

    private fun getExperienceLapStream(experienceId: String, lapId: String) {
//        useCase.getExperienceLapStream(experienceId).onEach { out ->
//            when (out) {
//                is Resource.Success -> {
//
//                    val streamsForSelectedLap = out.data?.find { it.lapId == lapId }
//
//                    chartSpinnerUI.experienceStream.value =
//                        streamsForSelectedLap?.toExperienceStream()
//                    chartSpinnerComparisonUI.experienceStream.value =
//                        streamsForSelectedLap?.toExperienceStream()
//
//                    out.data?.let {
//                        experiencePlayer.elevation.setStream(streamsForSelectedLap?.altitude)
//                        experiencePlayer.speed.setStream(streamsForSelectedLap?.velocitySmooth)
//                        experiencePlayer.distance.setStream(streamsForSelectedLap?.distance)
//                        updateFocusCoordinates(streamsForSelectedLap?.latLng)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
    }

    private fun getExperienceStream(experienceId: String) {
        useCase.getExperienceStream(experienceId).onEach { out ->
            when (out) {
                is Resource.Success -> {
                    chartSpinnerUI.experienceStream.value = out.data
                    chartSpinnerComparisonUI.experienceStream.value = out.data

                    out.data?.let {
                        experiencePlayer.updateStreams(
                            coordinateValues = it.latLng.toCoordinateModel(),
                            elevationValues = it.altitude,
                            speedValues = it.velocitySmooth,
                            distanceValues = it.distance,
                        )
                        setupObservers()
                        loadWebViewEvent.sendAction(Unit)

                        experienceHighlights = getRandomExperienceHighlights(it.latLng.size)
                        updateFocusCoordinates(it.latLng)
                        experienceHighlightsAddToMapEvent.sendAction(
                            getSampleExperienceHighlightSet(
                                _experience.value!!.id
                            )
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun updateFocusCoordinates(filtered: List<List<Double>>?) {
        _mapObservable.value = _mapObservable.value.updateFocusCoordinates(filtered)
        mapUIModel.setMapDisplayConfigFocusCoordinates(filtered)
        _mapObservable.value = _mapObservable.value.updateStartLatLonParams(
            startLatitude = filtered?.get(0)?.get(0),
            startLongitude = filtered?.get(0)?.get(1)
        )
    }

    private fun clearObservers() {
        experiencePlayer.status.removeObserver(experiencePlayerPlayStatusObserver)

        with(experiencePlayer.streams) {
            distanceStream.current.removeObserver(experiencePlayerDistanceObserver)
            elevationStream.current.removeObserver(experiencePlayerElevationObserver)
            speedStream.current.removeObserver(experiencePlayerSpeedObserver)
        }

    }

    private fun setupObservers() {
        experiencePlayer.status.observeForever(experiencePlayerPlayStatusObserver)

        with(experiencePlayer.streams) {
            distanceStream.current.observeForever(experiencePlayerDistanceObserver)
            elevationStream.current.observeForever(experiencePlayerElevationObserver)
            speedStream.current.observeForever(experiencePlayerSpeedObserver)
        }
    }

    fun onClickOpenShareTray() {
        _state.value = ExperienceMainState.OpenShareTray
    }

    fun onClickSettings() {
        _state.value = ExperienceMainState.ToggleSettingsDrawer(false)
    }

    fun onClickCloseSettings() {
        _state.value = ExperienceMainState.ToggleSettingsDrawer(true)
    }

    fun onClickPhotoSettings() {
        _experience.value?.let {
            _state.value = ExperienceMainState.GoToSettingsActivity(it)
        }
    }

    fun toggleExperienceDetailsItemsDraggable() {
        if (screenUIModel.toggleExperienceDetailsItemsDraggable()) {
            _state.value = ExperienceMainState.ExpandBottomSheet
        }
    }

    fun updateExperience(experience: Experience) {
        _experience.value = experience
    }

    fun getPhotoUrl(photoId: String, callback: (photoUrl: String) -> Unit) {
        photoUrlsById[photoId]?.let {
            callback(it)
        } ?: run {
            Log.i("JVFF", "retrieving image url from api")
            useCase.getExperiencePhoto(photoId).onEach { out ->
                when (out) {
                    is Resource.Success -> {
                        out.data?.photoUrl?.let {
                            photoUrlsById[photoId] = it
                            callback(it)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    companion object {
        const val EXPERIENCE_PLAYER_DURATION = 20000
    }
}
