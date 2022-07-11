package com.foltran.feature_experience.chart.di

import com.foltran.feature_experience.chart.presentation.ExperienceChartViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceChartModule {
    val instance = module {
        viewModel {
            ExperienceChartViewModel()
        }
    }
}
