package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaMap(
    val id: String,
    val polyline: String,
    val resource_state: Int,
    val summary_polyline: String
)