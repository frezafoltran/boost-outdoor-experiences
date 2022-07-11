package com.foltran.feature_experience.core.data.maps

import com.foltran.core_experience.shared.data.model.*
import com.foltran.feature_sync_strava.core.data.remote.models.strava.*

fun StravaAthleteXXXX.toAthlete() = Athlete(
    id = this.id.toString()
)

fun StravaAthleteXXXXX.toAthlete() = Athlete(
    id = this.id.toString()
)

fun StravaAthleteXXX.toAthlete() = Athlete(
    id = this.id.toString()
)

fun StravaAthleteXX.toAthlete() = Athlete(
    id = this.id.toString()
)

fun StravaBestEffort.toBestEffort() = BestEffort(
    id = this.id.toString()
)

fun StravaLap.toLap() = Lap(
    athlete = this.athlete.toAthlete(),
    averageCadence = this.average_cadence,
    averageHeartRate = this.average_heartrate,
    averageSpeed = this.average_speed,
    distance = this.distance,
    elapsedTime = this.elapsed_time,
    id = this.id.toString(),
    lapIndex = this.lap_index,
    maxHeartRate = this.max_heartrate,
    movingTime = this.moving_time,
    name = this.name,
    split = this.split,
    startDate = this.start_date,
    startDateLocal = this.start_date_local,
    totalElevationGain = this.total_elevation_gain,
    maxSpeed = this.max_speed,
    startIndex = this.start_index,
    endIndex = this.end_index
)

fun StravaMap.toMap() = com.foltran.core_experience.shared.data.model.Map(
    id = this.id,
    polyline = this.polyline,
    summaryPolyline = this.polyline
)

fun StravaMapX.toMap() = com.foltran.core_experience.shared.data.model.Map(
    id = this.id,
    summaryPolyline = this.summary_polyline
)

fun StravaSegment.toSegment() = Segment(
    activityType = this.activity_type,
    averageGrade = this.average_grade,
    city = this.city,
    country = this.country,
    state = this.state,
    distance = this.distance,
    elevationHigh = this.elevation_high,
    elevationLow = this.elevation_low,
    endLatitude = this.end_latitude,
    endLongitude = this.end_longitude,
    endLatLng = this.end_latlng,
    id = this.id.toString(),
    maximumGrade = this.maximum_grade,
    name = this.name,
    startLatitude = this.start_latitude,
    startLatLng = this.start_latlng,
    startLongitude = this.start_longitude
)

fun StravaSegmentEffort.toSegmentEffort() = SegmentEffort(
    athlete = this.athlete.toAthlete(),
    averageCadence = this.average_cadence,
    distance = this.distance,
    averageHeartRate = this.average_heartrate,
    elapsedTime = this.elapsed_time,
    id = this.id.toString(),
    maxHeartRate = this.max_heartrate,
    movingTime = this.moving_time,
    name = this.name,
    segment = this.segment.toSegment(),
    startDateLocal = this.start_date_local,
    startDate = this.start_date,
    startIndex = this.start_index
)

fun StravaActivity.toExperience() = Experience(
    athlete = this.athlete.toAthlete(),
    athleteCount = this.athlete_count,
    averageCadence = this.average_cadence,
    averageHeartRate = this.average_heartrate,
    averageSpeed = this.average_speed,
    averageTemp = this.average_temp,
    bestEfforts = this.best_efforts.map { it.toBestEffort() },
    calories = this.calories,
    commentCount = this.comment_count,
    distance = this.distance,
    elapsedTime = this.elapsed_time,
    elevHigh = this.elev_high,
    elevLow = this.elev_low,
    endLatLng = this.end_latlng,
    hasHeartRate = this.has_heartrate,
    id = this.id,
    laps = this.laps.map { it.toLap() },
    locationCity = this.location_city,
    locationCountry = this.location_country,
    locationState = this.location_state,
    map = this.map.toMap(),
    maxHeartRate = this.max_heartrate,
    maxSpeed = this.max_speed,
    movingTime = this.moving_time,
    name = this.name,
    photoCount = this.photo_count,
    segmentEfforts = this.segment_efforts.map { it.toSegmentEffort() },
    startDate = this.start_date,
    startDateLocal = this.start_date_local,
    startLatitude = this.start_latitude,
    startLongitude = this.start_longitude,
    startLatLng = this.start_latlng,
    timezone = this.timezone,
    totalElevationGain = this.total_elevation_gain,
    type = this.type,
    utcOffset = this.utc_offset,
    visibility = this.visibility
)