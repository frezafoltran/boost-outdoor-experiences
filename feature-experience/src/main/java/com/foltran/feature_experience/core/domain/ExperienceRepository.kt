package com.foltran.feature_experience.core.domain

import com.foltran.core_networking.core.models.Resource
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.core_experience.shared.data.model.LapStream
import kotlinx.coroutines.flow.Flow

interface ExperienceRepository {
    fun getExperience(id: String): Flow<Resource<Experience>>
    fun getExperienceStream(id: String): Flow<Resource<ExperienceStream>>
    fun getExperienceLapStream(experience_id: String): Flow<Resource<List<LapStream>>>
}