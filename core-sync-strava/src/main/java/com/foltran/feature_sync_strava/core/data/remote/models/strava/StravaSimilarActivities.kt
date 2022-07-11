package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaSimilarActivities(
    val average_speed: Double,
    val effort_count: Int,
    val frequency_milestone: Any,
    val max_average_speed: Double,
    val mid_average_speed: Double,
    val min_average_speed: Double,
    val pr_rank: Any,
    val resource_state: Int,
    val trend: StravaTrend
)