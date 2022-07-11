package com.foltran.core_map.experience.models.highlight


/**
 * Used to highlight segments of coordinates streams in the map. This class is used together
 * with ExperienceCoordinateStream to give a base reference for startIndex and endIndex
 */
data class PathHighlight(
    val startIndex: Int,
    val endIndex: Int,
    val triggered: Boolean = false
)

data class PathHighlightSet(
    val id: String,
    val highlights: List<PathHighlight>
)