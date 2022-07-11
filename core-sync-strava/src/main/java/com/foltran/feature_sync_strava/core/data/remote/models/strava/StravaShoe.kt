package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaShoe(
    val distance: Double,
    val id: String,
    val name: String,
    val primary: Boolean,
    val resource_state: Int
)