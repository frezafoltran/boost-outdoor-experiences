package com.foltran.core_map.experience.models.stream

import com.foltran.core_map.core.models.CoordinateModel

class ExperienceStreams(
    coordinateValues: List<CoordinateModel>,
    elevationValues: List<Double>,
    distanceValues: List<Double>,
    speedValues: List<Double>
) {

    private val coordinatesStream = ExperienceCoordinateStream(coordinateValues)
    val currentCoordinates get() = coordinatesStream.current
    val completeCoordinatesStream get() = coordinatesStream.values

    val elevationStream = ExperienceDoubleStream(elevationValues)

    val distanceStream = ExperienceDoubleStream(distanceValues)

    val speedStream = ExperienceDoubleStream(speedValues)

    fun updateCurrentItems(phase: Float) {

        phase.coerceAtLeast(0f).coerceAtMost(1f).let {
            coordinatesStream.updateByPhase(it)
            elevationStream.updateByPhase(it)
            distanceStream.updateByPhase(it)
            speedStream.updateByPhase(it)
        }

    }
}