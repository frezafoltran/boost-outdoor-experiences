package com.foltran.feature_experience.record.presentation

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExperienceStartRecordViewModel: ViewModel(), LifecycleObserver {

    private val _state = MutableLiveData("https://i.ibb.co/pz0S12s/Screenshot-20220224-130345-Maps.jpg")
    val state: LiveData<String> = _state
}