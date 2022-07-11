package com.foltran.core_map.core.presentation.model

data class MapCoreLap(
    val averageCadence: Double,
    val averageHeartRate: Double,
    val averageSpeed: Double,
    val distance: Double,
    val startIndex: Int,
    val endIndex: Int,
    val lapIndex: Int,
    val maxHeartRate: Int,
    val maxSpeed: Double,
    val movingTime: Int,
    val name: String,
    val startDate: String,
    val totalElevationGain: Double
)