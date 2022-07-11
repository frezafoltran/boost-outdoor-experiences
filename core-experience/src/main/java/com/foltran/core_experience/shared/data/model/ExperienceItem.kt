package com.foltran.core_experience.shared.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "experience_feed_items",
    indices = [Index(value = ["id"], unique = false)]
)
data class ExperienceItem(
    val averageSpeed: Double,
    val distance: Double,
    @PrimaryKey
    val id: String,
    @Embedded(prefix = "map_")
    val map: Map,
    val name: String,
    val locationCity: String?,
    val startDate: String,
    val startLatitude: Double,
    val startLongitude: Double
) {
    var indexInResponse: Int = -1
}

