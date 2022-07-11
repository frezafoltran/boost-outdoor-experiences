package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaSegmentEffort(
    val achievements: List<StravaAchievement>,
    val activity: StravaActivityXX,
    val athlete: StravaAthleteXXX,
    val average_cadence: Double,
    val average_heartrate: Double,
    val device_watts: Boolean,
    val distance: Double,
    val elapsed_time: Int,
    val end_index: Int,
    val hidden: Boolean,
    val id: Long,
    val max_heartrate: Int,
    val moving_time: Int,
    val name: String,
    val pr_rank: Int,
    val resource_state: Int,
    val segment: StravaSegment,
    val start_date: String,
    val start_date_local: String,
    val start_index: Int
)