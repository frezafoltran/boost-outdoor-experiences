package com.foltran.feature_profile.feed.di

import com.foltran.feature_profile.feed.presentation.ProfileFeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ProfileFeedModule {

    val instance = module {
        viewModel {
            ProfileFeedViewModel()
        }
    }
}