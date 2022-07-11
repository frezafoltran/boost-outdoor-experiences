package com.foltran.feature_experience.details.presentation.model

import androidx.lifecycle.MutableLiveData

class ExperienceViewPagerUIModel {
    val isTabBarVisible = MutableLiveData(true)
    val curAlpha = MutableLiveData(0f)
}