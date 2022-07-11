package com.foltran.feature_experience.feed.data.maps.strava

import com.foltran.feature_experience.core.data.maps.*
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaActivityListItem

fun StravaActivityListItem.toExperienceFeedItem() = ExperienceItem(
    averageSpeed = this.average_speed,
    distance = this.distance,
    id = this.id,
    locationCity = this.location_city,
    map = this.map.toMap(),
    name = this.name,
    startDate = this.start_date,
    startLatitude = this.start_latitude,
    startLongitude = this.start_longitude
)