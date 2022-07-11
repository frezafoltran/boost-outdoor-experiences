package com.foltran.core_map.experience.player

import androidx.lifecycle.LiveData
import com.foltran.core_map.experience.models.highlight.PathHighlightSet
import com.foltran.core_map.core.models.icon.MapIcon
import com.foltran.core_utils.observable.ActionLiveData

enum class ExperiencePlayerStaticDataEventStatuses{
    PLAYING,
    FINISHED
}

class ExperiencePlayerStaticData(
    var experienceHighlightSet: PathHighlightSet? = null,
    var photosByIndexInStream: Map<Int, MapIcon> = emptyMap(),
    var videosByIndexInStream: Map<Int, MapIcon> = emptyMap(),
) {

    private val _highlightEvent = ActionLiveData<ExperiencePlayerStaticDataEventStatuses>()
    val highlightEvent: LiveData<ExperiencePlayerStaticDataEventStatuses> = _highlightEvent

    fun playHighlightEvent() {
        _highlightEvent.sendActionAsync(ExperiencePlayerStaticDataEventStatuses.PLAYING)
    }

    fun finishHighlightEvent() {
        _highlightEvent.sendActionAsync(ExperiencePlayerStaticDataEventStatuses.FINISHED)
    }

    private val _photoEvent = ActionLiveData<String>()
    val photoEvent: LiveData<String> = _photoEvent

    fun playPhotoEvent(photoId: String) {
        _photoEvent.sendActionAsync(photoId)
    }

    private val _videoEvent = ActionLiveData<String>()
    val videoEvent: LiveData<String> = _videoEvent

    fun playVideoEvent(videoId: String) {
        _videoEvent.sendActionAsync("")
    }
}