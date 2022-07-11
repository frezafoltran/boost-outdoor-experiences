package com.foltran.core_navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Routes(val authority: String): Parcelable {
    @Parcelize
    object FeatureExperience: Routes(authority = "feature_experience")
}