package com.foltran.feature_experience.settings.photos.domain

import com.foltran.core_networking.core.models.Resource
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponse
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponseGet
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponsePost
import kotlinx.coroutines.flow.Flow

interface ExperienceSettingsPhotoRepository {
    fun putExperiencePhoto(base64: String,
                           photoLat: Double,
                           photoLon: Double,
                           experienceId: String,
                           indexInStream: Int
    ): Flow<Resource<PhotoResponsePost>>

    fun removeExperiencePhoto(photoId: String,
                           experienceId: String): Flow<Resource<PhotoResponse>>
    fun getExperiencePhoto(photoId: String): Flow<Resource<PhotoResponseGet>>
    fun getExperienceStream(id: String): Flow<Resource<ExperienceStream>>
}
