package com.foltran.feature_experience.settings.photos.di


import android.content.Context
import com.foltran.feature_experience.settings.photos.data.remote.ExperiencePhotoService
import com.foltran.feature_experience.settings.photos.data.repository.ExperienceSettingsPhotoRepositoryImpl
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ExperienceSettingsPhotoRepositoryModule {
    val instance = module {

        factory<ExperienceSettingsPhotoRepository> {
            ExperienceSettingsPhotoRepositoryImpl(service = get())
        }

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

fun provideServiceImpl(retrofit: Retrofit): ExperiencePhotoService = retrofit.create(
    ExperiencePhotoService::class.java
)

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