package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaTrend(
    val current_activity_index: Int,
    val direction: Int,
    val max_speed: Double,
    val mid_speed: Double,
    val min_speed: Double,
    val speeds: List<Double>
)