package com.foltran.feature_experience.feed.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.foltran.core_utils.bases.BaseViewModel
import com.foltran.core_utils.observable.ActionLiveData
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.feed.repository.ExperienceFeedRepository
import com.foltran.feature_experience.feed.presentation.adapter.ItemClickListener
import kotlinx.coroutines.ExperimentalCoroutinesApi


sealed class ExperienceFeedViewState{
    object Loading: ExperienceFeedViewState()
    data class Error(val error: String?): ExperienceFeedViewState()
    object Success: ExperienceFeedViewState()
    data class GoToCurFocusedExperience(val experience: Experience?): ExperienceFeedViewState()
}

sealed class ExperienceFeedFocusedExperienceState{
    object Loading: ExperienceFeedFocusedExperienceState()
    data class Error(val error: String?): ExperienceFeedFocusedExperienceState()
    object Success: ExperienceFeedFocusedExperienceState()
}

data class ItemClickParams(
    val experienceId: String,
    val itemPosition: Int,
)

class ExperienceFeedViewModel(
    val repository: ExperienceFeedRepository
): BaseViewModel(), ItemClickListener {

    private val _state = MutableLiveData<ExperienceFeedViewState>()
    val state: LiveData<ExperienceFeedViewState> = _state

    private val _isLoadingItemClick = MutableLiveData(false)
    val isLoadingItemClick: LiveData<Boolean> = _isLoadingItemClick

    val itemClickEvent = ActionLiveData<ItemClickParams>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val feed = repository.getExperiences(6).cachedIn(viewModelScope)

    override fun onResume() {
        super.onResume()
        _isLoadingItemClick.value = false
    }

    override fun onClickItem(item: ItemUI, position: Int) {
        itemClickEvent.sendAction(ItemClickParams(item.item.id, position))
    }
}