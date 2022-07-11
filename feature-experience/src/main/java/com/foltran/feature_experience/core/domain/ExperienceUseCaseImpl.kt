package com.foltran.feature_experience.core.domain

import com.foltran.core_networking.core.models.Resource
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.core_experience.shared.data.model.LapStream
import com.foltran.feature_experience.settings.photos.data.remote.PhotoResponseGet
import com.foltran.feature_experience.settings.photos.domain.ExperienceSettingsPhotoRepository
import kotlinx.coroutines.flow.Flow

interface ExperienceUseCase {
    fun getExperience(id: String): Flow<Resource<Experience>>
    fun getExperienceStream(id: String): Flow<Resource<ExperienceStream>>
    fun getExperienceLapStream(experience_id: String): Flow<Resource<List<LapStream>>>
    fun getExperiencePhoto(photoId: String): Flow<Resource<PhotoResponseGet>>
}

class ExperienceUseCaseImpl(
    private val repository: ExperienceRepository,
    private val photosRepository: ExperienceSettingsPhotoRepository
    ): ExperienceUseCase {

    override fun getExperience(id: String) = repository.getExperience(id)
    override fun getExperienceStream(id: String) = repository.getExperienceStream(id)
    override fun getExperienceLapStream(experience_id: String) = repository.getExperienceLapStream(experience_id)
    override fun getExperiencePhoto(photoId: String) = photosRepository.getExperiencePhoto(photoId)
}