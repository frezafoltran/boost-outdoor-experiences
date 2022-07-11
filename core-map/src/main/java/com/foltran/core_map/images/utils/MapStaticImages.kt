package com.foltran.core_map.images.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import com.foltran.core_localstorage.internalstorage.images.getFolderFromComplement
import com.foltran.core_localstorage.internalstorage.images.loadImageFromStorage
import com.foltran.core_localstorage.internalstorage.images.saveBitmapToInternalStorage
import com.foltran.core_localstorage.internalstorage.images.saveToInternalStorage
import com.foltran.core_map.R
import com.foltran.core_map.core.utils.style.defaultLineColorNoHash
import com.foltran.core_map.core.utils.style.highlightLineColorNoHash
import com.mapbox.api.staticmap.v1.MapboxStaticMap
import com.mapbox.api.staticmap.v1.StaticMapCriteria
import com.mapbox.api.staticmap.v1.models.StaticMarkerAnnotation
import com.mapbox.api.staticmap.v1.models.StaticPolylineAnnotation
import com.mapbox.core.utils.ColorUtils
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils

private fun getStaticPolylineAnnotations(polyline: String): StaticPolylineAnnotation =
    StaticPolylineAnnotation.builder()
        .strokeColor(defaultLineColorNoHash)
        .strokeWidth(4.0)
        .polyline(polyline)
        .build()

fun getStaticMap(polyline: String, height: Int?, width: Int?): String {

    val sample = listOf<StaticPolylineAnnotation>(getStaticPolylineAnnotations(polyline))

    return MapboxStaticMap.builder()
        //.accessToken(resources.getString(R.string.mapbox_access_token))
        .accessToken("TODO")
        .user("TODO")
        .styleId("cl0k7xz3x001814mnw9nuw3kf")
        //.styleId("ckxf1yws82bi914pqq6y2p6l4")
        .cameraAuto(true)
        .retina(true)
        .width(width ?: 400)
        .height(height ?: 400)
        .staticPolylineAnnotations(sample)
        .build().url().toString()
        .replace("@2x?access_token", "@2x?padding=50,50,50,50&access_token")
}

fun getStaticMapCleanStyle(polyline: String): String {

    val sample = listOf(getStaticPolylineAnnotations(polyline))

    return MapboxStaticMap.builder()
        .accessToken("TODO")
        .user("TODO")
        .styleId("cl299gvmj001415pqz4itfiut")
        .cameraAuto(true)
        .retina(true)
        .width(200)
        .height(200)
        .staticPolylineAnnotations(sample)
        .attribution(false)
        .build().url().toString()
        .replace("@2x?access_token", "@2x?padding=20,20,20,20&access_token")
}

fun getStaticMapUrlForHighlightBase(coordinates: List<List<Double>>): String {

    val points = coordinates.map { Point.fromLngLat(it[1], it[0]) }
    val styleCode = "cl299gvmj001415pqz4itfiut"

    val annotations = listOf(StaticPolylineAnnotation.builder()
        .strokeOpacity(1.0f)
        .strokeColor(defaultLineColorNoHash)
        .strokeWidth(4.0)
        .polyline(PolylineUtils.encode(points, 5))
        .build()
    )

    return MapboxStaticMap.builder()
        .accessToken("TODO")
        .user("TODO")
        .styleId(styleCode)
        .cameraAuto(true)
        .retina(true)
        .width(400)
        .height(400)
        .staticPolylineAnnotations(annotations)
        .build().url().toString()
        .replace("@2x?access_token", "@2x?padding=50,50,50,50&access_token")

}

fun getStaticMapUrlForHighlightSet(
    setCoordinates: List<List<List<Double>>>,
    polyline: String
): String {
    val styleCode = "ckxf1yws82bi914pqq6y2p6l4"

    val annotations = ArrayList<StaticPolylineAnnotation>().apply {

        //for size to be appropriate
        add(
            StaticPolylineAnnotation.builder()
                .strokeOpacity(0.1f)
                .strokeColor(highlightLineColorNoHash)
                .strokeWidth(6.0)
                .polyline(polyline)
                .build()
        )

        setCoordinates.forEach {
            val points = it.map { coord ->
                Point.fromLngLat(coord[1], coord[0])
            }
            add(
                StaticPolylineAnnotation.builder()
                    .strokeOpacity(1.0f)
                    .strokeColor(highlightLineColorNoHash)
                    .strokeWidth(6.0)
                    .polyline(PolylineUtils.encode(points, 5))
                    .build()
            )
        }
    }

    return MapboxStaticMap.builder()
        //.accessToken(resources.getString(R.string.mapbox_access_token))
        .accessToken("TODO")
        .user("TODO")
        .styleId(styleCode)
        .cameraAuto(true)
        .retina(true)
        .width(400)
        .height(400)
        .staticPolylineAnnotations(annotations)
        .build().url().toString()
        .replace("@2x?access_token", "@2x?padding=50,50,50,50&access_token")
}


data class LapBitmap(val bitmap: Bitmap, val lapId: String)

fun getStaticMapForHighlightsFromInternalStorage(
    experienceId: String?,
    highlightIds: List<String>?
): ArrayList<LapBitmap> {
    if (experienceId == null || highlightIds == null) return ArrayList()

    val out = ArrayList<LapBitmap>()

    highlightIds.forEachIndexed { index, id ->

        val curFile = "${experienceId}_${id}.png"

        val curBitmap: Bitmap? = loadImageFromStorage(curFile, experienceId)
        if (curBitmap == null) return@forEachIndexed
        out.add(
            LapBitmap(
                lapId = id,
                bitmap = curBitmap
            )
        )
    }

    loadImageFromStorage("${experienceId}_base.png", experienceId)?.let {
        out.add(
            LapBitmap(
                lapId = "-1",
                bitmap = it
            )
        )
    }
    return out

}

fun saveStaticMapForLapsInInternalStorage(
    experienceId: String?,
    bitmaps: List<LapBitmap>?,
    context: Context
): List<LapBitmap>? {
    if (experienceId == null || bitmaps == null) return null

    val out = ArrayList<LapBitmap>()

    bitmaps.forEach { elem ->

        val fileName = if (elem.lapId == "-1") "base" else elem.lapId

        val curFile = "${experienceId}_$fileName.png"
        val curFileAbsPath = "data/data/com.foltran.boost/app_imageDir/$curFile"

        saveBitmapToInternalStorage(elem.bitmap, curFile, experienceId, context)
        val curBitmap: Bitmap? = loadImageFromStorage(curFile, experienceId)
        if (curBitmap == null) return@forEach
        out.add(
            LapBitmap(
                lapId = elem.lapId,
                bitmap = curBitmap
            )
        )
    }
    return out
}
