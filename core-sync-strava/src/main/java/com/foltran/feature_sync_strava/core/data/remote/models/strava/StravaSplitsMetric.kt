package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaSplitsMetric(
    val average_grade_adjusted_speed: Double,
    val average_heartrate: Double,
    val average_speed: Double,
    val distance: Double,
    val elapsed_time: Int,
    val elevation_difference: Double,
    val moving_time: Int,
    val pace_zone: Int,
    val split: Int
)