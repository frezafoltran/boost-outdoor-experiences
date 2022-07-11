package com.foltran.core_experience.shared.data.model

import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaLapStream

data class LapStream(
    val lapId: String,
    val latLng: List<List<Double>>,
    val velocitySmooth: List<Double>,
    val gradeSmooth: List<Double>,
    val cadence: List<Int>,
    val distance: List<Double>,
    val altitude: List<Double>,
    val heartRate: List<Int>,
    val time: List<Int>
)

fun StravaLapStream.toLapStream() = LapStream(
    lapId = this.lap_id,
    latLng = this.latlng?.data.orEmpty(),
    velocitySmooth = this.velocity_smooth?.data.orEmpty(),
    gradeSmooth = this.grade_smooth?.data.orEmpty(),
    cadence = this.cadence?.data.orEmpty(),
    distance = this.distance?.data.orEmpty(),
    altitude = this.altitude?.data.orEmpty(),
    heartRate = this.heartrate?.data.orEmpty(),
    time = this.time?.data.orEmpty(),
)

fun LapStream.toExperienceStream() = ExperienceStream(
    latLng = this.latLng,
    velocitySmooth = this.velocitySmooth,
    gradeSmooth = this.gradeSmooth,
    cadence = this.cadence,
    distance = this.distance,
    altitude = this.altitude,
    heartRate = this.heartRate,
    time = this.time,
)