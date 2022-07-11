package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaLap(
    val activity: StravaActivityX,
    val athlete: StravaAthleteXX,
    val average_cadence: Double,
    val average_heartrate: Double,
    val average_speed: Double,
    val device_watts: Boolean,
    val distance: Double,
    val elapsed_time: Int,
    val end_index: Int,
    val id: Long,
    val lap_index: Int,
    val max_heartrate: Int,
    val max_speed: Double,
    val moving_time: Int,
    val name: String,
    val pace_zone: Int,
    val resource_state: Int,
    val split: Int,
    val start_date: String,
    val start_date_local: String,
    val start_index: Int,
    val total_elevation_gain: Double
)