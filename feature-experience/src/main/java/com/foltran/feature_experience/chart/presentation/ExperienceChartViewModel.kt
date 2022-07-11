package com.foltran.feature_experience.chart.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foltran.core_utils.bases.BaseViewModel

sealed class ExperienceChartState {
    object CloseActivity: ExperienceChartState()
}

class ExperienceChartViewModel: BaseViewModel() {

    private val _state = MutableLiveData<ExperienceChartState>()
    val state: LiveData<ExperienceChartState> = _state

    fun onClose() {
        _state.value = ExperienceChartState.CloseActivity
    }
}