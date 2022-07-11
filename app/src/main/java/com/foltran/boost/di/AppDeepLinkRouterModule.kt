package com.foltran.boost.di

import com.foltran.core_navigation.Routable
import com.foltran.feature_experience.navigation.ExperienceRouter
import org.koin.dsl.module

object AppDeepLinkRouterModule {
    val instance = module {
        factory {
            HashMap<String, Routable<*, *>>().apply {
                put(ExperienceRouter.deepLinkAuthority, ExperienceRouter)
            }
        }
    }
}