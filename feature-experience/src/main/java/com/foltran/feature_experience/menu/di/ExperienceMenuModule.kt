package com.foltran.feature_experience.menu.di

import com.foltran.core_utils.dispatchers.QualifierDispatcherIO
import com.foltran.feature_experience.core.data.repository.ExperienceRepositoryImpl
import com.foltran.feature_experience.core.domain.ExperienceRepository
import com.foltran.feature_experience.menu.domain.ExperienceMenuUseCase
import com.foltran.feature_experience.menu.domain.ExperienceMenuUseCaseImpl
import com.foltran.feature_experience.menu.presentation.ExperienceMenuViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceMenuModule {

    val instance = module {
        viewModel {
            ExperienceMenuViewModel(
                useCase = get()
            )
        }

        factory<ExperienceMenuUseCase> {
            ExperienceMenuUseCaseImpl(
                repository = get(),
                dispatcher = get(QualifierDispatcherIO)
            )
        }

        single(QualifierDispatcherIO) { Dispatchers.IO }

        factory<ExperienceRepository> {
            ExperienceRepositoryImpl(service = get())
        }
    }
}