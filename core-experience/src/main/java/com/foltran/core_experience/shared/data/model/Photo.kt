package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Photo(
    val id: String,
    val lat: Double,
    val lon: Double,
    val indexInStream: Int
) : Parcelable
