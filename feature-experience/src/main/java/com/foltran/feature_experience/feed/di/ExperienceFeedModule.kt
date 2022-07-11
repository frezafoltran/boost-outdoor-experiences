package com.foltran.feature_experience.feed.di

import android.app.Application
import com.foltran.core_experience.feed.repository.ExperienceFeedRepository
import com.foltran.core_experience.feed.repository.ExperienceFeedRepositoryImpl
import com.foltran.core_experience.room.db.ExperienceFeedDb
import com.foltran.feature_experience.feed.presentation.ExperienceFeedViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ExperienceFeedModule {

    val instance = module {
        viewModel {
            ExperienceFeedViewModel(
                repository = get()
            )
        }
        factory {
            provideDataBase(androidApplication())
        }
        factory<ExperienceFeedRepository> {
            ExperienceFeedRepositoryImpl(
                service = get(),
                db = get()
            )
        }
    }
}

fun provideDataBase(application: Application): ExperienceFeedDb {
    return ExperienceFeedDb.create(application)
}

