package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Map(
    val id: String? = null,
    val polyline: String? = null,
    val summaryPolyline: String? = null
) : Parcelable
