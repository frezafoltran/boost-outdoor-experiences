package com.foltran.feature_experience.settings.photos.domain

import com.foltran.core_networking.core.models.Resource
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponse
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponseGet
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponsePost
import kotlinx.coroutines.flow.Flow

interface ExperienceSettingsPhotoUseCase {
    fun getExperienceStream(experienceId: String): Flow<Resource<ExperienceStream>>
    fun getExperiencePhoto(photoId: String): Flow<Resource<PhotoResponseGet>>
    fun removeExperiencePhoto(photoId: String, experienceId: String): Flow<Resource<PhotoResponse>>
    fun putExperiencePhoto(
        base64: String,
        photoLat: Double,
        photoLon: Double,
        experienceId: String,
        indexInStream: Int
    ): Flow<Resource<PhotoResponsePost>>
}

class ExperienceSettingsPhotoUseCaseImpl(val repository: ExperienceSettingsPhotoRepository) :
    ExperienceSettingsPhotoUseCase {

    override fun getExperienceStream(experienceId: String) =
        repository.getExperienceStream(experienceId)

    override fun getExperiencePhoto(photoId: String) = repository.getExperiencePhoto(photoId)

    override fun removeExperiencePhoto(photoId: String, experienceId: String) =
        repository.removeExperiencePhoto(photoId, experienceId)

    override fun putExperiencePhoto(
        base64: String,
        photoLat: Double,
        photoLon: Double,
        experienceId: String,
        indexInStream: Int
    ) = repository.putExperiencePhoto(base64, photoLat, photoLon, experienceId, indexInStream)
}