package com.foltran.feature_experience.navigation

import com.foltran.core_navigation.RouterData
import com.foltran.core_navigation.Routes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceRouterData(
    val beatle: String? = ""
) : RouterData() {
    companion object {
        val ROUTE_AUTHORITY = Routes.FeatureExperience.authority
    }
}