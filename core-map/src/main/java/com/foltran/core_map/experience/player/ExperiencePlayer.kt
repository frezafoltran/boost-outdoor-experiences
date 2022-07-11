package com.foltran.core_map.experience.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.foltran.core_map.core.models.CoordinateModel
import com.foltran.core_map.experience.models.stream.ExperienceStreams

class ExperiencePlayer(
    val duration: Int,
    coordinateValues: List<CoordinateModel> = emptyList(),
    elevationValues: List<Double> = emptyList(),
    speedValues: List<Double> = emptyList(),
    distanceValues: List<Double> = emptyList(),
    private val onPhotoEvent: (photoId: String, callback: () -> Unit) -> Unit,
    private val onVideoEvent: (videoId: String, callback: () -> Unit) -> Unit,
    ) {

    fun updateStreams(coordinateValues: List<CoordinateModel>,
                      elevationValues: List<Double>,
                      speedValues: List<Double>,
                      distanceValues: List<Double>){

        streams = ExperienceStreams(
            coordinateValues = coordinateValues,
            elevationValues = elevationValues,
            speedValues = speedValues,
            distanceValues = distanceValues
        )
    }

    var streams = ExperienceStreams(
        coordinateValues = coordinateValues,
        elevationValues = elevationValues,
        speedValues = speedValues,
        distanceValues = distanceValues
    )

    private val _status = MutableLiveData(DEFAULT_STATUS)
    val status: LiveData<ExperiencePlayerStatuses> = _status

    fun setStatusReadyToPlay() {
        _status.value = ExperiencePlayerStatuses.READY
    }

    val statusPhotoEventObserver = Observer<String> { photoId ->
        _status.value = ExperiencePlayerStatuses.PAUSED
        _isStatusBlocked.value = true

        onPhotoEvent.invoke(photoId) {
            _status.value = ExperiencePlayerStatuses.PLAYING
            _isStatusBlocked.value = false
        }
    }

    val statusVideoEventObserver = Observer<String> { videoId ->
        _status.value = ExperiencePlayerStatuses.PAUSED
        _isStatusBlocked.value = true

        onVideoEvent.invoke(videoId) {
            _status.value = ExperiencePlayerStatuses.PLAYING
            _isStatusBlocked.value = false
        }
    }

    private var _isStatusBlocked = MutableLiveData(false)
    val isStatusBlocked get() = _isStatusBlocked

    private val curPhaseObserver = Observer<Float> {
        if (it.toInt() == FINISH_PHASE) {
            _status.value = ExperiencePlayerStatuses.FINISHED
        }

        streams.updateCurrentItems(it)
    }

    val curPhase = MutableLiveData<Float>().also {
        it.observeForever(curPhaseObserver)
    }

    fun pause() {
        if (_isStatusBlocked.value == false && _status.value != ExperiencePlayerStatuses.PAUSED) {
            _status.value = ExperiencePlayerStatuses.PAUSED
        }
    }

    fun start() {
        if (_isStatusBlocked.value == false && _status.value != ExperiencePlayerStatuses.PLAYING) {
            _status.value = ExperiencePlayerStatuses.PLAYING
        }
    }

    fun finish() {
        _status.value = ExperiencePlayerStatuses.FINISHED
    }

    companion object {
        private val DEFAULT_STATUS = ExperiencePlayerStatuses.LOADING
        private const val FINISH_PHASE = 1
    }
}
