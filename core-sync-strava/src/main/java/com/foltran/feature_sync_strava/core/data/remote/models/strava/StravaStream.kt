package com.foltran.feature_sync_strava.core.data.remote.models.strava

data class StravaStreamItemLatLng(
    val data: List<List<Double>>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemVelocitySmooth(
    val data: List<Double>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemGradeSmooth(
    val data: List<Double>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemCadence(
    val data: List<Int>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemDistance(
    val data: List<Double>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemAltitude(
    val data: List<Double>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemHeartRate(
    val data: List<Int>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStreamItemTime(
    val data: List<Int>,
    val series_type: String,
    val original_size: Int,
    val resolution: String
)

data class StravaStream(
    val latlng: StravaStreamItemLatLng?,
    val velocity_smooth: StravaStreamItemVelocitySmooth?,
    val grade_smooth: StravaStreamItemGradeSmooth?,
    val cadence: StravaStreamItemCadence?,
    val distance: StravaStreamItemDistance?,
    val altitude: StravaStreamItemAltitude?,
    val heartrate: StravaStreamItemHeartRate?,
    val time: StravaStreamItemTime?
)

data class StravaLapStream(
    val lap_id: String,
    val latlng: StravaStreamItemLatLng?,
    val velocity_smooth: StravaStreamItemVelocitySmooth?,
    val grade_smooth: StravaStreamItemGradeSmooth?,
    val cadence: StravaStreamItemCadence?,
    val distance: StravaStreamItemDistance?,
    val altitude: StravaStreamItemAltitude?,
    val heartrate: StravaStreamItemHeartRate?,
    val time: StravaStreamItemTime?
)