package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lap(
    val athlete: Athlete,
    val averageCadence: Double,
    val averageHeartRate: Double,
    val averageSpeed: Double,
    val distance: Double,
    val elapsedTime: Int,
    val id: String,
    val lapIndex: Int,
    val maxHeartRate: Int,
    val maxSpeed: Double,
    val movingTime: Int,
    val name: String,
    val split: Int,
    val startDate: String,
    val startDateLocal: String,
    val totalElevationGain: Double,
    val startIndex: Int,
    val endIndex: Int
) : Parcelable