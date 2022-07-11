package com.foltran.feature_profile.core.di

import android.app.Application
import com.foltran.core_experience.feed.repository.ExperienceFeedRepository
import com.foltran.core_experience.feed.repository.ExperienceFeedRepositoryImpl
import com.foltran.core_experience.profile.repository.ProfileFeedRepository
import com.foltran.core_experience.profile.repository.ProfileFeedRepositoryImpl
import com.foltran.core_experience.room.db.ExperienceFeedDb
import com.foltran.feature_profile.core.presentation.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ProfileModule {

    val instance = module {
        viewModel {
            ProfileViewModel(
                repository = get()
            )
        }

        factory {
            provideDataBase(androidApplication())
        }
        factory<ProfileFeedRepository> {
            ProfileFeedRepositoryImpl(
                service = get(),
                db = get()
            )
        }
    }
}

fun provideDataBase(application: Application): ExperienceFeedDb {
    return ExperienceFeedDb.create(application)
}
