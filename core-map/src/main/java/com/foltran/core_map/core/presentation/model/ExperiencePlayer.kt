package com.foltran.core_map.core.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.foltran.core_map.R
import com.foltran.core_utils.formatting.meter
import com.foltran.core_utils.formatting.meterPerSecondToMinPerKmPace
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.core_utils.observable.ActionLiveData

enum class PlayStatuses {
    PAUSED,
    PLAYING,
    STOPPED,
    BLOCKED,
    LOADING,
    START_HIGHLIGHT,
    END_HIGHLIGHT,
}

enum class ExperienceHighlightEventStatus {
    STARTED,
    FINISHED
}
data class ExperienceHighlightEvent(
    val status: ExperienceHighlightEventStatus,
)

data class ExperiencePlayer(
    var cameraAltitude: Int = 3000,
    var duration: Int = 20000
) {

    val playStatus: MutableLiveData<PlayStatuses> = MutableLiveData(PlayStatuses.LOADING)

    val coordinates = ExperiencePlayerStream.Coordinates()
    val elevation = ExperiencePlayerStream.Elevation()
    val speed = ExperiencePlayerStream.Speed()
    val distance = ExperiencePlayerStream.Distance()

    private var lastRefreshedText: Long = -1

    private val curPhaseObserver = Observer<Float> {
        if (it.toInt() == 1) {
            playStatus.value = PlayStatuses.STOPPED
        }
    }
    val curPhase = MutableLiveData<Float>().also {
        it.observeForever(curPhaseObserver)
    }

    val shouldShowStopButton = MutableLiveData(false)

    val pendingHighlightEvent = ActionLiveData<ExperienceHighlightEvent>()


    val playPauseIcon = MutableLiveData(R.drawable.ic_play)

    val isMainBearingVisible: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(playStatus) {
            val newValue = it != PlayStatuses.START_HIGHLIGHT
            if (newValue != value) value = newValue
        }
    }


    fun startOrPause(){
        playStatus.value = when (playStatus.value!!) {
            PlayStatuses.PLAYING -> PlayStatuses.PAUSED
            PlayStatuses.PAUSED, PlayStatuses.STOPPED -> PlayStatuses.PLAYING
            else -> playStatus.value
        }
    }

    fun stop() {
        playStatus.value = PlayStatuses.STOPPED
    }

    init {
        curPhase.observeForever {

            val curIndex: Int =  coordinates.stream.value?.let { coordinates ->
                (it * coordinates.size.toFloat()).toInt()
            } ?: return@observeForever

            with (speed) {
                val range = if(maxSpeed - minSpeed == 0.0) 1.0 else maxSpeed - minSpeed
                curRatioOfSpeed.value = (stream.value?.get(curIndex) ?: 0.0) / range
            }
            with (distance) {
                curProgress.value = ((((stream.value?.get(curIndex) ?: 0.0) - minDistance) / distanceDelta) * 100).toInt()
            }

            // update of text
            val curTime = System.currentTimeMillis()
            if (curTime > lastRefreshedText + MAX_REFRESH_RATE_TEXT) {
                with (elevation) {
                    curValStr.value = stream.value?.get(curIndex)?.meter()
                }
                with (speed) {
                    curValStr.value = stream.value?.get(curIndex)?.meterPerSecondToMinPerKmPace()
                }
                with (distance) {
                    curValStr.value = stream.value?.get(curIndex)?.meterToKm()
                }
                lastRefreshedText = curTime
            }
        }

        playStatus.observeForever{
            when (it) {
                PlayStatuses.PLAYING -> {
                    playPauseIcon.value = R.drawable.ic_pause
                    shouldShowStopButton.value = true
                }
                PlayStatuses.PAUSED -> {
                    playPauseIcon.value = R.drawable.ic_play
                    shouldShowStopButton.value = true
                }
                PlayStatuses.STOPPED -> {
                    playPauseIcon.value = R.drawable.ic_play
                    shouldShowStopButton.value = false
                }
                PlayStatuses.BLOCKED -> {
                    playPauseIcon.value = R.drawable.ic_block
                    shouldShowStopButton.value = false
                }
                PlayStatuses.LOADING -> {
                    playPauseIcon.value = R.drawable.ic_player_loader
                    shouldShowStopButton.value = false
                }
                PlayStatuses.START_HIGHLIGHT -> {
                    pendingHighlightEvent.sendAction(
                        ExperienceHighlightEvent(
                            status = ExperienceHighlightEventStatus.STARTED
                        )
                    )
                }
                PlayStatuses.END_HIGHLIGHT -> {
                    pendingHighlightEvent.sendAction(
                        ExperienceHighlightEvent(
                            status = ExperienceHighlightEventStatus.FINISHED
                        )
                    )
                    playStatus.value = PlayStatuses.PLAYING
                }
            }
        }
    }

    companion object {
        private val MAX_REFRESH_RATE_TEXT = 500
    }

}