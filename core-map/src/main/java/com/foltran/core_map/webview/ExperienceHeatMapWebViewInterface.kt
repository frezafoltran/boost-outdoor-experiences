package com.foltran.core_map.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.foltran.core_map.experience.webview.BaseWebViewInterface
import com.google.gson.Gson
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class ExperienceItemHeatMapExcerpt(
    val startCoordinates: List<Double>,
    val polyline: String
)

@Suppress("unused")
class ExperienceHeatMapWebViewInterface(
    private val experiences: List<ExperienceItemHeatMapExcerpt>
): BaseWebViewInterface() {

    private fun distanceBetweenPoints(p1: List<Double>, p2: List<Double>) =
        sqrt((p1[0] - p2[0]).pow(2) + (p1[1] - p2[1]).pow(2))

    private fun formatCoordinates(): List<List<Double>> =
        experiences.map {
            listOf(it.startCoordinates[1], it.startCoordinates[0])
        }

    @JavascriptInterface
    fun getClosestPolylineForClick(lat: Double, lon: Double): String {
        var curMin = Double.MAX_VALUE
        val refPoint = listOf(lat, lon)
        var outPolyline = ""

        experiences.forEach {
            val d = distanceBetweenPoints(refPoint, it.startCoordinates)
            if (d < curMin) {
                curMin = d
                outPolyline = it.polyline
            }
        }
        Log.i("JVFF", outPolyline)
        return outPolyline
    }

    @JavascriptInterface
    fun getStartCoordinates() = formatCoordinates().toString()

    @JavascriptInterface
    fun getPolylines() = Gson().toJson(experiences.map { it.polyline })

    @JavascriptInterface
    fun logS(s: String) {
        Log.i("JVFF", s)
    }

    override val jsLabel = "ExperienceHeatMap"
    override val htmlPath = "html/compose_map.html"
    override val jsPaths = listOf(
        "js/fit_to_coordinates.js",
        "js/polyline_utils.js",
        "js/compose_map_heatmap.js",
    )
}