package com.foltran.core_map.core.presentation.model

import com.mapbox.maps.EdgeInsets

const val BASE_MAP_CORE_PADDING = 150.0

/**
 * This data class contains the data the map needs to build the display. These can change
 * as the user interacts with it
 */
data class MapCoreDisplayConfig(
    val mainPolyline: String? = null,
    val focusCoordinates: List<List<Double>>? = null,
    val padding: MapCorePadding = MapCorePadding(),
    val pitch: Double = 0.0,
    val bearing: Double = 0.0
)

data class MapCorePadding(
    val top: Double = 0.0,
    val right: Double = 0.0,
    val bottom: Double = 0.0,
    val left: Double = 0.0
)

fun MapCorePadding.toEdgeInsets() = EdgeInsets(
    this.top + BASE_MAP_CORE_PADDING,
    this.left + BASE_MAP_CORE_PADDING,
    this.bottom + BASE_MAP_CORE_PADDING,
    this.right + BASE_MAP_CORE_PADDING,
)

fun MapCorePadding.toEdgeInsetsZeroBase() = EdgeInsets(
    this.top ,
    this.left,
    this.bottom,
    this.right,
)