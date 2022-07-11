package com.foltran.core_experience.shared.data.model

import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaStream



data class ExperienceStream(
    val latLng: List<List<Double>>,
    val velocitySmooth: List<Double>,
    val gradeSmooth: List<Double>,
    val cadence: List<Int>,
    val distance: List<Double>,
    val altitude: List<Double>,
    val heartRate: List<Int>,
    val time: List<Int>
)

fun StravaStream.toExperienceStream() = ExperienceStream(
    latLng = this.latlng?.data.orEmpty(),
    velocitySmooth = this.velocity_smooth?.data.orEmpty(),
    gradeSmooth = this.grade_smooth?.data.orEmpty(),
    cadence = this.cadence?.data.orEmpty(),
    distance = this.distance?.data.orEmpty(),
    altitude = this.altitude?.data.orEmpty(),
    heartRate = this.heartrate?.data.orEmpty(),
    time = this.time?.data.orEmpty(),
)
