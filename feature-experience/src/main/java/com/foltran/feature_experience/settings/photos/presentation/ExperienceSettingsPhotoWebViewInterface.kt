package com.foltran.feature_experience.settings.photos.presentation

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.foltran.core_map.core.utils.getCoordinatesFromPolyline
import com.foltran.core_map.experience.webview.BaseWebViewInterface
import com.foltran.core_map.webview.MapWebViewBaseInterface
import com.foltran.core_map.webview.Photo
import com.foltran.core_utils.observable.ActionLiveData
import com.google.gson.Gson

@Suppress("unused")
class ExperienceSettingsPhotoWebViewInterface(
    private val startLngLat: List<Double>,
    private val polyline: String?,
    private val currentLat: MutableLiveData<Double>,
    private val currentLng: MutableLiveData<Double>,
    private val currentPhotos: MutableLiveData<Map<String, Photo>>,
    private val hasPendingPhotosChange: MutableLiveData<Boolean>
) : BaseWebViewInterface() {


    @JavascriptInterface
    fun getStartLngLatString() = listOf(startLngLat[1], startLngLat[0]).toString()

    private fun formatCoordinatesStream(): List<List<Double>> =
        polyline?.getCoordinatesFromPolyline()?.map {
            listOf(it[1], it[0])
        } ?: listOf()

    @JavascriptInterface
    fun getCoordinatesStreamString() = formatCoordinatesStream().toString()


    @JavascriptInterface
    fun getCurrentLat(): Double = currentLat.value ?: -1000.0

    @JavascriptInterface
    fun getCurrentLng(): Double = currentLng.value ?: -1000.0


    @JavascriptInterface
    fun hasPendingChanges() = hasPendingPhotosChange.value == true


    @JavascriptInterface
    fun resetPendingChanges() {
        hasPendingPhotosChange.postValue(false)
    }

    @JavascriptInterface
    fun getCurrentPhotosString(): String = Gson().toJson(currentPhotos.value ?: emptyMap<String, Photo>())


    override val jsLabel = "SlideInterface"
    override val htmlPath = "html/experience_animation.html"
    override val jsPaths = listOf("js/experience_slide_center.js", "js/experience_photo_utils.js")
}