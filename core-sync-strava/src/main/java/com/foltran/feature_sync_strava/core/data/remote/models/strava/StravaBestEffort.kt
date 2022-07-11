package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaBestEffort(
    val achievements: List<Any>,
    val activity: StravaActivityXXX,
    val athlete: StravaAthleteX,
    val distance: Int,
    val elapsed_time: Int,
    val end_index: Int,
    val id: Long,
    val moving_time: Int,
    val name: String,
    val pr_rank: Any,
    val resource_state: Int,
    val start_date: String,
    val start_date_local: String,
    val start_index: Int
)