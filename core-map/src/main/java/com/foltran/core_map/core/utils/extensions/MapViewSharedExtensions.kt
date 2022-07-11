package com.foltran.core_map.core.utils.extensions

import android.graphics.Bitmap
import android.os.Parcelable
import android.view.View
import com.foltran.core_map.R
import com.foltran.core_map.webview.ExperienceHighlight
import com.mapbox.maps.MapView


fun View.addStartBeatleToMap(latitude: Double, longitude: Double) {
    with(this as MapView) {
        addIconToMap(latitude, longitude, R.drawable.logo_beatle)
    }
}

data class ExperienceHighlightSet(
    val experienceHighlightSetId: String,
    val highlights: List<ExperienceHighlight>
)

fun View.addHighlightsToMap(experienceHighlightSet: ExperienceHighlightSet, coordinateStream: List<List<Double>>?) {

    if (coordinateStream == null) return

    val coordinateSegments = experienceHighlightSet.highlights.map {
        coordinateStream.subList(it.startIndex, it.endIndex)
    }

    with (this as MapView) {
        addListOfCoordinatesToMap(coordinateSegments)
    }
}

fun View.addVideoBeatleToMap(latitude: Double, longitude: Double, thumbnailBitmap: Bitmap) {
    with(this as MapView) {
        addBitMapToMap(latitude, longitude, thumbnailBitmap)
    }
}

