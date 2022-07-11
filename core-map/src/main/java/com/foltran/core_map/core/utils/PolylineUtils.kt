package com.foltran.core_map.core.utils

import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.maps.*


fun MapboxMap.getCameraPositionFromPolyline(
    polyline: String,
    padding: EdgeInsets,
    bearing: Double,
    pitch: Double
) = cameraForCoordinates(
    coordinates = PolylineUtils.decode(polyline, 5),
    padding = padding,
    bearing = bearing,
    pitch = pitch
)

fun String.getCoordinatesFromPolyline(): List<List<Double>> =
    PolylineUtils.decode(this, 5).map {
        listOf(it.latitude(), it.longitude())
    }
