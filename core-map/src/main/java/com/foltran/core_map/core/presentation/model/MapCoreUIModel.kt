package com.foltran.core_map.core.presentation.model

import com.foltran.core_map.webview.Photo

/**
 * This model contains the data that the map needs to build itself
 */
data class MapCoreUIModel(
    val polyline: String? = null,
    val focusCoordinates: List<List<Double>>? = null,
    val laps: List<MapCoreLap>? = null,
    val startLatitude: Double = 0.0,
    val startLongitude: Double = 0.0,
    val photos: List<Photo> = emptyList(),
    val pictureUrls: List<String> = listOf(
        "https://i.ibb.co/sFrSLBh/IMG-20180516-120058.jpg",
        "https://i.ibb.co/jy2C0QY/IMG-20180516-103109.jpg",
    ),
    val pictures: List<MapCorePicture> = listOf(
        MapCorePicture(
            url = "https://i.ibb.co/sFrSLBh/IMG-20180516-120058.jpg",
            latitude = 40.7699,
            longitude = -73.97335
        ),
        MapCorePicture(
            url = "https://i.ibb.co/jy2C0QY/IMG-20180516-103109.jpg",
            latitude = 40.77411,
            longitude = -73.96932
        )
    )
)

data class MapCorePicture(
    val url: String,
    val latitude: Double,
    val longitude: Double
)