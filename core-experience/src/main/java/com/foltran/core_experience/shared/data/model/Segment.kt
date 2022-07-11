package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Segment(
    val activityType: String,
    val averageGrade: Double,
    val city: String?,
    val country: String?,
    val state: String?,
    val distance: Double,
    val elevationHigh: Double,
    val elevationLow: Double,
    val endLatitude: Double,
    val endLatLng: List<Double>,
    val endLongitude: Double,
    val id: String,
    val maximumGrade: Double,
    val name: String,
    val startLatitude: Double,
    val startLatLng: List<Double>,
    val startLongitude: Double
) : Parcelable