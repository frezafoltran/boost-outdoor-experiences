package com.foltran.core_navigation

import org.koin.dsl.module

object RouteModule {
    val instance = module {
        single {
            Router(routes = get())
        }
    }
}