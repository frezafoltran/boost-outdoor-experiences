package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SegmentEffort(
    val athlete: Athlete,
    val averageCadence: Double,
    val averageHeartRate: Double,
    val distance: Double,
    val elapsedTime: Int,
    val id: String,
    val maxHeartRate: Int,
    val movingTime: Int,
    val name: String,
    val segment: Segment,
    val startDate: String,
    val startDateLocal: String,
    val startIndex: Int
) : Parcelable