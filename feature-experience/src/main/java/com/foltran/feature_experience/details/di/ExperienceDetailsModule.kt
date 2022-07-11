package com.foltran.feature_experience.details.di

import com.foltran.feature_experience.details.presentation.ExperienceDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceDetailsModule {
    val instance = module {
        viewModel {
            ExperienceDetailsViewModel()
        }
    }
}
