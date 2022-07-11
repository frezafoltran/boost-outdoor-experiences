package com.foltran.core_map.core.models.icon


/**
 * This model is used by the player to display the video icons in the map. It is provided to the
 * Javascript interface through a map with keys being the index in stream where the photo is at.
 */
data class MapIcon(
    val id: String,
    val lat: Double,
    val lon: Double,
    val indexInStream: Int,
    val base64: String
)

fun List<MapIcon>?.toMapIconByIndexInStream() =
    this?.map { it.indexInStream to it }?.toMap() ?: emptyMap()