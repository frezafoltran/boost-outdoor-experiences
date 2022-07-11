package com.foltran.feature_experience.settings.photos.di

import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoUseCase
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoUseCaseImpl
import com.foltran.feature_experience.settings.photos.presentation.ExperienceSettingsPhotosViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private object ExperienceSettingsPhotosModule {
    val instance = module {
        viewModel { (experience: Experience) ->
            ExperienceSettingsPhotosViewModel(
                useCase = get(),
                initialExperience = experience
            )
        }

        factory<ExperienceSettingsPhotoUseCase> {
            ExperienceSettingsPhotoUseCaseImpl(repository = get())
        }
    }
}

val ExperienceSettingsPhotosModules = arrayOf(
    ExperienceSettingsPhotosModule.instance,
    ExperienceSettingsPhotoRepositoryModule.instance
)
