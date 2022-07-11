package com.foltran.feature_experience.record.di

import com.foltran.feature_experience.record.presentation.ExperienceStartRecordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceRecordModule {

    val instance = module {
        viewModel {
            ExperienceStartRecordViewModel()
        }

    }
}
