package com.foltran.feature_experience.core.presentation

import androidx.lifecycle.MutableLiveData
import com.foltran.feature_experience.details.presentation.model.ExperienceViewPagerUIModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface ExperienceScreenUIModel {

    val experienceViewPagerUI: MutableLiveData<ExperienceViewPagerUIModel>
    fun updateViewPagerOnBottomSheetChange(slideOffset: Float)

    val experienceDetailsItemsDraggable: MutableLiveData<Boolean>
    fun toggleExperienceDetailsItemsDraggable(): Boolean

    val experienceChartSpinnerOptions : List<String>
    val experienceComparisonChartSpinnerOptions: List<String>
}

class ExperienceScreenUIModelImpl: ExperienceScreenUIModel {

    override val experienceViewPagerUI = MutableLiveData(ExperienceViewPagerUIModel())


    override fun updateViewPagerOnBottomSheetChange(slideOffset: Float) {

        if (slideOffset <= 0.5) {
            experienceViewPagerUI.value?.isTabBarVisible?.value = true
        }

        experienceViewPagerUI.value?.let{
            with (it){
                if (slideOffset > 0.52f) {
                    if (isTabBarVisible.value == true) {
                        isTabBarVisible.value = false
                    }
                    curAlpha.value = (slideOffset - 0.52f) * 10f
                }
                if (slideOffset <= 0.52f && isTabBarVisible.value != true) {
                    isTabBarVisible.value = true
                    curAlpha.value = 0f
                }
            }
        }
    }

    override val experienceDetailsItemsDraggable = MutableLiveData(false)

    override fun toggleExperienceDetailsItemsDraggable(): Boolean {
        val newValue = experienceDetailsItemsDraggable.value?.not() == true
        experienceDetailsItemsDraggable.value = newValue
        return newValue
    }

    override val experienceChartSpinnerOptions = listOf("pace", "heart rate", "cadence", "elevation")

    override val experienceComparisonChartSpinnerOptions = listOf("pace compared to similar runs")

}