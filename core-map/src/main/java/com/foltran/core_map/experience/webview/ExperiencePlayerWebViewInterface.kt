package com.foltran.core_map.experience.webview

import android.webkit.JavascriptInterface
import com.foltran.core_map.core.models.toMapboxFormat
import com.foltran.core_map.core.presentation.model.PlayStatuses
import com.foltran.core_map.experience.player.ExperiencePlayer
import com.foltran.core_map.experience.player.ExperiencePlayerStaticData
import com.foltran.core_map.experience.player.ExperiencePlayerStatuses
import com.foltran.core_utils.observable.ActionLiveData

@Suppress("unused")
/**
 *  This class bridges the javascript code to play an experience using a WebView and the Android app.
 *  The motivation to have part of the mapping logic in JS is because the Turf library in Mapbox is
 *  more well documented in JS at this time.
 *
 *  @param mapCorePlayer: ExperiencePlayer
 *  @param mapCorePlayerStaticData: ExperiencePlayerStaticData
 *
 *  It receives a mapCorePlayer that manages the data that changes over time
 *  (distance, speed streams...), as well as access to the player's status.
 *
 *  It also receives mapCorePlayerFixedData that manages the data that does not change over time
 *  (photos, videos, start and end points, highlight locations).
 *
 *  @author frezafoltran - 06/24/2022
 */
class ExperiencePlayerWebViewInterface(
    private val mapCorePlayer: ExperiencePlayer,
    private val mapCorePlayerStaticData: ExperiencePlayerStaticData,
) : BaseWebViewInterface() {

    val mapLoadedEvent = ActionLiveData<Unit>()

    init {
        mapCorePlayerStaticData.photoEvent.observeForever(mapCorePlayer.statusPhotoEventObserver)
        mapCorePlayerStaticData.videoEvent.observeForever(mapCorePlayer.statusVideoEventObserver)

        mapLoadedEvent.observeForever {
            mapCorePlayer.setStatusReadyToPlay()
        }
    }

    @JavascriptInterface
    fun getPlayerDuration() = mapCorePlayer.duration

    @JavascriptInterface
    fun isPlayerStatusPlaying(): Boolean = mapCorePlayer.status.value == ExperiencePlayerStatuses.PLAYING

    @JavascriptInterface
    fun isPlayerStatusPaused(): Boolean = mapCorePlayer.status.value == ExperiencePlayerStatuses.PAUSED

    @JavascriptInterface
    fun isPlayerStatusFinished(): Boolean = mapCorePlayer.status.value == ExperiencePlayerStatuses.FINISHED

    @JavascriptInterface
    fun playHighlightEvent() {
        mapCorePlayerStaticData.playHighlightEvent()
    }

    @JavascriptInterface
    fun finishHighlightEvent() {
        mapCorePlayerStaticData.finishHighlightEvent()
    }

    @JavascriptInterface
    fun playPhotoEvent(photoIndex: Int) {
        with(mapCorePlayerStaticData) {
            photosByIndexInStream[photoIndex]?.id?.let {
                playPhotoEvent(it)
            }
        }
    }

    //TODO this will have same format as photo event
    @JavascriptInterface
    fun playVideoEvent() {
        with(mapCorePlayerStaticData) {
            playVideoEvent("")
        }
    }

    @JavascriptInterface
    fun updateAnimationPhase(phase: Float) {
        mapCorePlayer.curPhase.postValue(phase)
    }

    @JavascriptInterface
    fun getCoordinateStream() =
        mapCorePlayer.streams.completeCoordinatesStream.toMapboxFormat().toString()

    @JavascriptInterface
    fun getExperienceHighlights(): String =
        mapCorePlayerStaticData.experienceHighlightSet?.jsonParse() ?: ""

    @JavascriptInterface
    fun getPhotosByIndexInCoordinateStream(): String =
        mapCorePlayerStaticData.photosByIndexInStream.jsonParse()

    @JavascriptInterface
    fun getPhotosIndexesSorted(): String =
        mapCorePlayerStaticData.photosByIndexInStream.keys.sorted().toString()

    @JavascriptInterface
    fun getVideosByIndexInCoordinateStream(): String =
        mapCorePlayerStaticData.videosByIndexInStream.jsonParse()

    @JavascriptInterface
    fun getVideosIndexesSorted(): String =
        mapCorePlayerStaticData.videosByIndexInStream.keys.sorted().toString()


    @JavascriptInterface
    fun issueMapLoaded() {
        mapLoadedEvent.sendActionAsync(Unit)
    }

    override val jsLabel = "ExperiencePlayer"
    override val htmlPath = "${ROOT_ASSET_FOLDER}/html/main.html"
    override val jsPaths = listOf(
        "${ROOT_ASSET_FOLDER}/js/fit_to_coordinates.js",
        "${ROOT_ASSET_FOLDER}/js/models.js",
        "${ROOT_ASSET_FOLDER}/js/setup.js",
        "${ROOT_ASSET_FOLDER}/js/render_utils.js",
        "${ROOT_ASSET_FOLDER}/js/render_data.js",
        "${ROOT_ASSET_FOLDER}/js/setup.js",
        "${ROOT_ASSET_FOLDER}/js/highlights.js",
        "${ROOT_ASSET_FOLDER}/js/main.js",
    )

    companion object {
        const val ROOT_ASSET_FOLDER = "experience_player"
    }

}