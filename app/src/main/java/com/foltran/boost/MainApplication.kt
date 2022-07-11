package com.foltran.boost

import android.app.Application
import com.foltran.boost.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class MainApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin()
    }

    private fun startKoin() {
        if (GlobalContext.getOrNull() == null) {
            org.koin.core.context.startKoin {
                androidLogger(
                    level = Level.ERROR
                )
                androidContext(this@MainApplication)
                modules(AppModule.modules)
            }
        }
    }
}