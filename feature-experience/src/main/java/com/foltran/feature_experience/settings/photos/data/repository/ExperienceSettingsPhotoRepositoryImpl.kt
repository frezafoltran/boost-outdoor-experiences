package com.foltran.feature_experience.settings.photos.data.repository

import com.foltran.core_networking.core.models.Resource
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.core_experience.shared.data.model.toExperienceStream
import com.foltran.feature_experience.settings.photos.data.remote.*
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class ExperienceSettingsPhotoRepositoryImpl(val service: ExperiencePhotoService) :
    ExperienceSettingsPhotoRepository {
    override fun putExperiencePhoto(
        base64: String,
        photoLat: Double,
        photoLon: Double,
        experienceId: String,
        indexInStream: Int
    ): Flow<Resource<PhotoResponsePost>> = flow {
        try {
            emit(Resource.Loading<PhotoResponsePost>())

            service.putExperiencePhoto(
                PhotoBody(
                    base64 = base64,
                    photoLat = photoLat,
                    photoLon = photoLon,
                    experienceId = experienceId,
                    indexInStream = indexInStream
                )
            ).let { response ->
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error<PhotoResponsePost>("An unexpected error occurred"))
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error<PhotoResponsePost>(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<PhotoResponsePost>("Couldn't reach server. Check your internet connection and try again"))
        }
    }

    override fun getExperiencePhoto(photoId: String): Flow<Resource<PhotoResponseGet>> = flow {

        try {
            emit(Resource.Loading<PhotoResponseGet>())

            service.getExperiencePhotoUrl(photoId).let { response ->
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error<PhotoResponseGet>("An unexpected error occurred"))
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error<PhotoResponseGet>(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<PhotoResponseGet>("Couldn't reach server. Check your internet connection and try again"))
        }
    }

    override fun removeExperiencePhoto(
        photoId: String,
        experienceId: String
    ): Flow<Resource<PhotoResponse>> = flow {
        try {
            emit(Resource.Loading<PhotoResponse>())

            service.deleteExperiencePhoto(
                PhotoBodyDelete(
                    photoId = photoId,
                    experienceId = experienceId
                )
            ).let { response ->
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.Success(response.body()!!))
                } else {
                    emit(Resource.Error<PhotoResponse>("An unexpected error occurred"))
                }
            }

        } catch (e: HttpException) {
            emit(Resource.Error<PhotoResponse>(e.localizedMessage ?: "An error occurred"))
        } catch (e: IOException) {
            emit(Resource.Error<PhotoResponse>("Couldn't reach server. Check your internet connection and try again"))
        }
    }

    override fun getExperienceStream(id: String): Flow<Resource<ExperienceStream>> = flow {

        try {
            emit(Resource.Loading<ExperienceStream>())

            service.getStreams(id).let{ response ->
                if (response.isSuccessful && response.body() != null){
                    emit(Resource.Success(response.body()!!.toExperienceStream()))
                }
                else{
                    emit(Resource.Error<ExperienceStream>("An unexpected error occurred"))
                }
            }

        } catch(e: HttpException) {
            emit(Resource.Error<ExperienceStream>(e.localizedMessage ?: "An error occurred"))
        }
        catch(e: IOException) {
            emit(Resource.Error<ExperienceStream>("Couldn't reach server. Check your internet connection and try again"))
        }
    }
}