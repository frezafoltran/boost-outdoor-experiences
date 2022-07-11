package com.foltran.feature_experience.core.di

import com.foltran.feature_experience.core.data.repository.ExperienceRepositoryImpl
import com.foltran.feature_experience.core.domain.ExperienceRepository
import com.foltran.feature_experience.core.domain.ExperienceUseCase
import com.foltran.feature_experience.core.domain.ExperienceUseCaseImpl
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.core.presentation.*
import com.foltran.feature_experience.settings.photos.di.ExperienceSettingsPhotoRepositoryModule
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private object ExperienceMainModule {
    val instance = module {
        viewModel { (experienceFromParam: Experience) ->
            ExperienceViewModel(
                useCase = get(),
                mapUIModel = get(),
                screenUIModel = get(),
                experienceFromParam = experienceFromParam
            )
        }
        factory<ExperienceUseCase> {
            ExperienceUseCaseImpl(repository = get(), photosRepository = get())
        }
        factory<ExperienceRepository> {
            ExperienceRepositoryImpl(service = get())
        }
        factory<ExperienceMapUIModel> {
            ExperienceMapUIModelImpl()
        }
        factory<ExperienceScreenUIModel> {
            ExperienceScreenUIModelImpl()
        }
    }
}

val ExperienceMainModules = arrayOf(
    ExperienceSettingsPhotoRepositoryModule.instance,
    ExperienceMainModule.instance
)