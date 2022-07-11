package com.foltran.boost.di


import com.foltran.boost.presentation.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainActivityModule {

    val instance = module {
        viewModel {
            MainActivityViewModel()
        }

    }
}

