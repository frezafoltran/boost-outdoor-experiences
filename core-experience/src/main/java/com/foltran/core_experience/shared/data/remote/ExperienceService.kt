package com.foltran.core_experience.shared.data.remote

import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaLapStream
import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaStream
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExperienceService {
    @GET("experiences_feed")
    suspend fun getExperiences(): Response<List<ExperienceItem>>

    @GET("experiences/{id}")
    suspend fun getExperience(@Path("id") id: String): Response<Experience>

    @GET("strava/s3/get_streams/{id}")
    suspend fun getStreams(@Path("id") id: String): Response<StravaStream>

    @GET("strava/s3/lap_streams/{experience_id}")
    suspend fun getExperienceLapStreams(
        @Path("experience_id") experience_id: String
    ): Response<List<StravaLapStream>>
}