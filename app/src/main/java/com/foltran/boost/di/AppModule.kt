package com.foltran.boost.di

import com.foltran.core_navigation.RouteModule
import com.foltran.feature_experience.core.di.ExperienceCoreModule

object AppModule {

    val modules = listOf(
        RouteModule.instance,
        AppDeepLinkRouterModule.instance,
        MainActivityModule.instance,
        ExperienceCoreModule.instance
    )
}