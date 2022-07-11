package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaSegment(
    val activity_type: String,
    val average_grade: Double,
    val city: String,
    val climb_category: Int,
    val country: String,
    val distance: Double,
    val elevation_high: Double,
    val elevation_low: Double,
    val elevation_profile: Any,
    val end_latitude: Double,
    val end_latlng: List<Double>,
    val end_longitude: Double,
    val hazardous: Boolean,
    val id: Int,
    val maximum_grade: Double,
    val name: String,
    val `private`: Boolean,
    val resource_state: Int,
    val starred: Boolean,
    val start_latitude: Double,
    val start_latlng: List<Double>,
    val start_longitude: Double,
    val state: String
)