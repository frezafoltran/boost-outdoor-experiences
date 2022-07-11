package com.foltran.feature_profile.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.foltran.core_experience.profile.repository.ProfileFeedExperienceSortingOptions
import com.foltran.core_experience.profile.repository.ProfileFeedRepository
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.bases.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileViewModel(val repository: ProfileFeedRepository): BaseViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedByDate = repository.getExperiences(6, ProfileFeedExperienceSortingOptions.Date).cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedByDistance = repository.getExperiences(6, ProfileFeedExperienceSortingOptions.Distance).cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val feedByLocation = repository.getExperiences(6, ProfileFeedExperienceSortingOptions.Location).cachedIn(viewModelScope)

    private val _feedItems = MutableLiveData<List<ExperienceItem>>()
    val feedItems: LiveData<List<ExperienceItem>> = _feedItems

    init {
        repository.getExperiences().onEach {
            when (it) {
                is Resource.Success -> {
                    _feedItems.value = it.data
                }
            }
        }.launchIn(viewModelScope)
    }


}