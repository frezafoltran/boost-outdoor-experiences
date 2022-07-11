package com.foltran.feature_experience.menu.presentation

import androidx.lifecycle.*
import com.foltran.core_map.images.utils.LapBitmap
import com.foltran.core_networking.core.models.Resource
import com.foltran.core_utils.bases.BaseViewModel
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.getSampleExperienceHighlights
import com.foltran.feature_experience.menu.domain.ExperienceMenuUseCase
import com.foltran.feature_experience.menu.presentation.adapters.ExperienceMenuLapsSelector
import com.foltran.feature_experience.menu.presentation.adapters.LapSelectorData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed class ExperienceMenuState {
    object Loading : ExperienceMenuState()
    object Success : ExperienceMenuState()
    data class Error(val message: String?) : ExperienceMenuState()
    data class GoToExperience(
        val experience: Experience?
    ) : ExperienceMenuState()
}

data class ImageToSave(
    val experienceId: String,
    val bitmaps: List<LapBitmap>,
    val shouldSave: Boolean
)

class ExperienceMenuViewModel(
    private val useCase: ExperienceMenuUseCase
) : BaseViewModel(), ExperienceMenuLapsSelector {

    private val _state = MutableLiveData<ExperienceMenuState>()
    val state: LiveData<ExperienceMenuState> = _state

    private val _experience = MutableLiveData<Experience>()
    val experience: LiveData<Experience> = _experience

    private val experienceObserver = Observer<Experience> {
        viewModelScope.launch {
            useCase.syncBitmapsForExperience(
                experienceId = it.id,
                experienceHighlights = it.experienceHighlights ?: emptyList(),
                experiencePolyline = it.map.polyline ?: ""
            )
        }
    }

    val focusedExperienceLapBitmaps = MutableLiveData<List<LapBitmap>>()

    val focusedExperienceLapBitmap = MutableLiveData<LapBitmap>()
    val focusedExperienceBaseBitmap = MutableLiveData<LapBitmap>()
    val focusedExperienceLapSelections = MutableLiveData<List<LapSelectorData>>()

    val focusedExperienceLoading: LiveData<Boolean> = _state.map {
        it is ExperienceMenuState.Loading
    }

    val saveImageByUrlToInternalStorage = useCase.saveImageByUrlToInternalStorage


    init {

        _experience.observeForever(experienceObserver)

        focusedExperienceLapBitmaps.observeForever {
            if (it.isNotEmpty()) {
                focusedExperienceLapBitmap.value = it[0]
                focusedExperienceBaseBitmap.value = it.find { it.lapId.contains("base") }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _experience.removeObserver(experienceObserver)
    }

    override fun setCurrentSelected(index: Int) {
        focusedExperienceLapBitmaps.value?.let {
            if (index < 0 || index >= it.size) return

            focusedExperienceLapBitmap.value = it[index]
        }
    }

    fun goToExperience() {
        _experience.value?.let { experienceVal ->

            //_state.value = ExperienceMenuState.GoToExperience(_experience.value, experienceStreamParams)
            _state.value = ExperienceMenuState.GoToExperience(_experience.value)
        }
    }

    fun updateLaps(laps: List<LapBitmap>) {

        val sortedLaps = laps.sortedBy { it.lapId }

        focusedExperienceBaseBitmap.value = sortedLaps.find { it.lapId == "-1" }
        focusedExperienceLapBitmaps.value = sortedLaps

        focusedExperienceLapSelections.value =
            sortedLaps.mapIndexed { index, lapBitmap ->
                val label =
                    if (index == 0) "default" else "pace highlights"

                val lap = _experience.value?.laps?.find {
                    lapBitmap.lapId.contains(it.id)
                }

                if (label == "all laps") {
                    LapSelectorData(label, _experience.value?.distance, _experience.value?.elapsedTime)
                } else {
                    LapSelectorData(label, lap?.distance, lap?.elapsedTime)
                }
            }
        _state.value = ExperienceMenuState.Success
    }

    fun updateCurFocusedExperience(experienceId: String) {

        useCase.getExperience(experienceId).onEach {
            when (it) {
                is Resource.Loading -> _state.value = ExperienceMenuState.Loading
                is Resource.Error -> _state.value = ExperienceMenuState.Error(it.message)
                is Resource.Success -> {

                    it.data?.let { experience ->
                        _experience.value = experience.copy(
                            experienceHighlights = getSampleExperienceHighlights(experience.id)
                        )
                    }

                }
            }
        }.launchIn(viewModelScope)
    }

}