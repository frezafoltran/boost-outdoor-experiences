package com.foltran.feature_experience.core.di

import android.content.Context
import com.foltran.core_experience.shared.data.remote.ExperienceService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ExperienceCoreModule {
    val instance = module {
        single {
            providesRetrofitAdapter(
                httpClient = get(),
                gson = get(),
                endPoint = "TODO"
            )
        }

        single { provideServiceImpl(retrofit = get()) }

        single { provideCachedOkHttpClient(context = androidContext()) }
        single { provideGson() }
    }
}

fun provideServiceImpl(retrofit: Retrofit): ExperienceService = retrofit.create(ExperienceService::class.java)

fun provideGson(): Gson = GsonBuilder().create()

fun providesRetrofitAdapter(
    gson: Gson,
    httpClient: OkHttpClient,
    endPoint: String
): Retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(endPoint)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
    .build()

fun provideCachedOkHttpClient(context: Context): OkHttpClient {

    val cacheSize = (5 * 1024 * 1024).toLong()  //5Mb cache limit
    val myCache = Cache(context.cacheDir, cacheSize)

    return OkHttpClient.Builder()
        .cache(myCache)
        .build()
}
