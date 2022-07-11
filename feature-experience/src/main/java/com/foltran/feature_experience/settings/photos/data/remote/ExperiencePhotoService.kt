package com.foltran.feature_experience.settings.photos.data.remote

import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_sync_strava.core.data.remote.models.strava.StravaStream
import retrofit2.Response
import retrofit2.http.*

data class PhotoResponse(
    val updatedExperience: Experience
)

data class PhotoResponsePost(
    val updatedExperience: Experience,
    val photoAddedId: String
)

data class PhotoResponseGet(
    val photoUrl: String
)

data class PhotoBody(
    val photoLat: Double,
    val photoLon: Double,
    val indexInStream: Int,
    val base64: String,
    val experienceId: String
)

data class PhotoBodyDelete(
    val photoId: String,
    val experienceId: String
)

interface ExperiencePhotoService {
    @GET("experiences/photo/{id}")
    suspend fun getExperiencePhotoUrl(
        @Path("id") id: String
    ): Response<PhotoResponseGet>

    @POST("experiences/photo")
    suspend fun putExperiencePhoto(
        @Body body: PhotoBody
    ): Response<PhotoResponsePost>

    @POST("experiences/photo/delete")
    suspend fun deleteExperiencePhoto(
        @Body body: PhotoBodyDelete
    ): Response<PhotoResponse>

    @GET("strava/s3/get_streams/{id}")
    suspend fun getStreams(@Path("id") id: String): Response<StravaStream>
}
