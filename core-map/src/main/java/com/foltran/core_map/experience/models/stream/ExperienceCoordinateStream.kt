package com.foltran.core_map.experience.models.stream

import com.foltran.core_map.core.models.CoordinateModel

data class ExperienceCoordinateStream(val values: List<CoordinateModel>): ExperienceBaseStream {

    private val size = values.size.toFloat()

    private var _current: CoordinateModel? = null
    val current get() = _current

    override fun updateByPhase(phase: Float) {
        _current = values[(size * phase).toInt()]
    }
}