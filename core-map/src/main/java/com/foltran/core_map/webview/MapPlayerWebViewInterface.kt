package com.foltran.core_map.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.foltran.core_map.core.presentation.model.ExperiencePlayer
import com.foltran.core_map.core.presentation.model.PlayStatuses
import com.foltran.core_map.core.utils.extensions.ExperienceHighlightSet
import com.foltran.core_map.core.utils.style.defaultLineColor
import com.foltran.core_map.core.utils.style.highlightLineColor
import com.foltran.core_utils.extensions.round
import com.foltran.core_utils.observable.ActionLiveData
import com.google.gson.Gson

data class ExperienceHighlight(
    val startIndex: Int,
    val endIndex: Int
)

fun getRandomExperienceHighlights(experienceStreamSize: Int): List<ExperienceHighlight> =
    mutableListOf<ExperienceHighlight>().also {

        val highlightLength = 50
        if (experienceStreamSize < 3 * highlightLength) return@also

        val dividerLow = (experienceStreamSize / 3.0).toInt()
        val dividerHigh = ((2.0 * experienceStreamSize) / 3.0).toInt()

        (0..(dividerLow - highlightLength)).random().let { start ->
            it.add(
                ExperienceHighlight(
                    startIndex = start,
                    endIndex = start + highlightLength
                )
            )
        }

        (dividerLow..(dividerHigh - highlightLength)).random().let { start ->
            it.add(
                ExperienceHighlight(
                    startIndex = start,
                    endIndex = start + highlightLength
                )
            )
        }

        (dividerHigh..(experienceStreamSize - highlightLength)).random().let { start ->
            it.add(
                ExperienceHighlight(
                    startIndex = start,
                    endIndex = start + highlightLength
                )
            )
        }

    }

data class Photo(
    val id: String,
    var url: String? = null,
    val lat: Double,
    val lon: Double,
    val indexInStream: Int
)

fun List<Photo>.toPhotoMapByIndexInStream() =
    this.map { it.indexInStream to it }.toMap()

fun latLonPairToId(lat: Double, lon: Double) = "${lat.round(4)}_${lon.round(4)}"

fun List<Photo>.toPhotoMapByLatLng() =
    this.map { latLonPairToId(it.lat, it.lon) to it }.toMap()

abstract class MapWebViewBaseInterface {
    abstract val jsLabel: String
    abstract val htmlPath: String
    abstract val jsPaths: List<String>
}

class MapPlayerWebViewInterface(
    private val startLngLat: List<Double>? = null,
    private val photosByIndexInStream: Map<Int, Photo> = emptyMap(),
    private val photosByLatLng: Map<String, Photo> = emptyMap(),
    private val mapCorePlayer: ExperiencePlayer,
    private val streamSize: Int,
    private val experienceHighlightSet: ExperienceHighlightSet?,
    private val videoIconsBase64ById: Map<String, String>
) : MapWebViewBaseInterface() {

    var showPhotoByIdEvent = ActionLiveData<String>()
    var showVideoByIdEvent = ActionLiveData<String>()

    var mapAlreadyLoaded = false
    var mapLoadedEvent = ActionLiveData<Unit>()

    var previousCoordinateIndex = -1

    val showedPicturesKey = HashSet<String>()

    @JavascriptInterface
    fun logS(s: String) {
        Log.i("JVFF", s)
    }

    @JavascriptInterface
    fun issueMapLoaded() {
        if (!mapAlreadyLoaded) {
            mapLoadedEvent.sendActionAsync(Unit)
        }
    }

    @JavascriptInterface
    fun getCameraAltitude() = mapCorePlayer.cameraAltitude

    @JavascriptInterface
    fun getAnimationDuration() = mapCorePlayer.duration

    private fun formatCoordinatesStream(): List<List<Double>> =
        mapCorePlayer.coordinates.stream.value?.map {
            listOf(it[1], it[0])
        } ?: emptyList()

    @JavascriptInterface
    fun getCoordinatesStreamSimplifiedString() = formatCoordinatesStream().toString()

    @JavascriptInterface
    fun getVideoIconsBase64ById(): String = Gson().toJson(videoIconsBase64ById)

    @JavascriptInterface
    fun getPhotosMapString(): String = Gson().toJson(photosByIndexInStream)

    @JavascriptInterface
    fun getPhotosIndexListString(): String = Gson().toJson(photosByIndexInStream.keys.sorted())

    @JavascriptInterface
    fun getStartLngLatString() = startLngLat.toString()

    @JavascriptInterface
    fun isMapPlaying(): Boolean = mapCorePlayer.playStatus.value == PlayStatuses.PLAYING

    @JavascriptInterface
    fun isMapPaused(): Boolean =
        mapCorePlayer.playStatus.value == PlayStatuses.PAUSED

    @JavascriptInterface
    fun isMapBlocked(): Boolean = mapCorePlayer.playStatus.value == PlayStatuses.BLOCKED

    @JavascriptInterface
    fun isMapStopped(): Boolean = mapCorePlayer.playStatus.value == PlayStatuses.STOPPED

    @JavascriptInterface
    fun isMapHighlight(): Boolean = mapCorePlayer.playStatus.value == PlayStatuses.START_HIGHLIGHT

    @JavascriptInterface
    fun sendHighlightEvent() {
        mapCorePlayer.playStatus.postValue(PlayStatuses.START_HIGHLIGHT)
    }

    @JavascriptInterface
    fun removeHighlightEvent() {
        mapCorePlayer.playStatus.postValue(PlayStatuses.END_HIGHLIGHT)
    }

    @JavascriptInterface
    fun updateCurPhase(phase: Float) {
        mapCorePlayer.curPhase.postValue(phase)
    }

    @JavascriptInterface
    fun showPhotoAtIndex(index: Int) {
        val photoId = photosByIndexInStream[index]?.id

        if (photoId.isNullOrEmpty().not()) {
            showPhotoByIdEvent.sendActionAsync(photoId!!)
        }
    }

    @JavascriptInterface
    fun showVideo() {
        showVideoByIdEvent.sendActionAsync("")
    }

    @JavascriptInterface
    fun getExperienceHighlightsString(): String = Gson().toJson(experienceHighlightSet)

    @JavascriptInterface
    fun getHighlightLineColor() = highlightLineColor

    @JavascriptInterface
    fun getDefaultLineColor() = defaultLineColor

    override val jsLabel = "AndroidMapPlayer"
    override val htmlPath = "html/experience_animation.html"
    override val jsPaths = listOf(
        "js/fit_to_coordinates.js",
        "js/experience_highlight_utils.js",
        "js/mapbox_camera_utils.js",
        "js/experience_photo_utils.js",
        "js/experience_animation.js"
    )

}