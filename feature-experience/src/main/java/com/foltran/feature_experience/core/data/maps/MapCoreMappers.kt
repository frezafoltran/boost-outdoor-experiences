package com.foltran.feature_experience.core.data.maps

import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.Lap
import com.foltran.core_map.core.presentation.model.MapCoreLap
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.feature_experience.core.domain.model.toWebViewPhotos


fun Lap.toMapCoreLap() = MapCoreLap(
    averageCadence = this.averageCadence,
    averageHeartRate = this.averageHeartRate,
    averageSpeed = this.averageSpeed,
    distance = this.distance,
    startIndex = this.startIndex,
    endIndex = this.endIndex,
    lapIndex = this.lapIndex,
    maxHeartRate = this.maxHeartRate,
    maxSpeed = this.maxSpeed,
    movingTime = this.movingTime,
    name = this.name,
    startDate = this.startDate,
    totalElevationGain = this.totalElevationGain
)

fun MapCoreUIModel?.updateExperiencePolylineParams(experience: Experience?): MapCoreUIModel = this?.copy(
    polyline = experience?.map?.polyline
) ?: MapCoreUIModel(
    photos = experience?.photos?.toWebViewPhotos()?: emptyList(),
    polyline = experience?.map?.polyline,
    laps = experience?.laps?.map{ it.toMapCoreLap() }
)

fun MapCoreUIModel?.updateStartLatLonParams(startLongitude: Double?, startLatitude: Double?): MapCoreUIModel = this?.copy(
    startLongitude = startLongitude ?: 0.0,
    startLatitude = startLatitude ?: 0.0
) ?: MapCoreUIModel(
    startLongitude = startLongitude ?: 0.0,
    startLatitude = startLatitude ?: 0.0
)

fun MapCoreUIModel?.updateFocusCoordinates(focusCoordinates: List<List<Double>>?): MapCoreUIModel = this?.copy(
    focusCoordinates = focusCoordinates
) ?: MapCoreUIModel(
    focusCoordinates = focusCoordinates
)
