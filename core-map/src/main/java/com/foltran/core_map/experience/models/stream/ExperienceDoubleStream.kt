package com.foltran.core_map.experience.models.stream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

data class ExperienceDoubleStream(val values: List<Double>): ExperienceBaseStream {

    private val size = values.size.toFloat()
    private val maxValue = values.maxOrNull()
    private val minValue = values.minOrNull()

    private var _current = MutableLiveData<Double>()
    val current: LiveData<Double> = _current

    val currentRatio: LiveData<Double> = Transformations.map(current) {
        if (maxValue == null || minValue == null || maxValue == minValue) 0.0
        else (it - minValue) / (maxValue - minValue)
    }

    val currentProgress: LiveData<Int> = Transformations.map(currentRatio) {
        (it * 100).toInt()
    }

    override fun updateByPhase(phase: Float) {
        _current.value = values[(size * phase).toInt()]
    }
}