package com.foltran.feature_experience.map.di

import com.foltran.feature_experience.feed.presentation.ExperienceFeedViewModel
import com.foltran.feature_experience.map.presentation.ExperienceMapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceMapModule {
    val instance = module {
        viewModel {
            ExperienceMapViewModel()
        }

    }
}
