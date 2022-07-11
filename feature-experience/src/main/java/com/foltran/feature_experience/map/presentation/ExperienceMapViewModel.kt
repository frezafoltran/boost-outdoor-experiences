package com.foltran.feature_experience.map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCoreMainView
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_utils.bases.BaseViewModel


sealed class ExperienceMapState {
    object UpdateMapCamera: ExperienceMapState()
    object UpdateMapPolyline: ExperienceMapState()
    object UpdateMapFocusCoordinates: ExperienceMapState()
    object UpdateMapStartBeatle: ExperienceMapState()
    object InitTransitionToPlayMap: ExperienceMapState()
}

class ExperienceMapViewModel: BaseViewModel() {

    private val _state = MutableLiveData<ExperienceMapState>()
    val state: LiveData<ExperienceMapState> = _state

    var previousMapState: MapCoreUIModel? = null
    private var previousMapDisplayConfigState: MapCoreDisplayConfig? = null

    var isMapShown = MutableLiveData(false)
    var isPicturesShown = MutableLiveData(false)
    var isWebViewShown = MutableLiveData(false)


    fun mapObserver(newState: MapCoreUIModel){

        if (previousMapState?.polyline != newState.polyline){
            _state.value = ExperienceMapState.UpdateMapPolyline
        }
        if (previousMapState?.startLongitude != newState.startLongitude || previousMapState?.startLatitude != newState.startLatitude) {
            _state.value = ExperienceMapState.UpdateMapStartBeatle
        }
        if (previousMapState?.focusCoordinates != newState.focusCoordinates) {
            _state.value = ExperienceMapState.UpdateMapFocusCoordinates
        }
        previousMapState = newState
    }

    fun mapDisplayConfigObserver(newState: MapCoreDisplayConfig){

        _state.value = ExperienceMapState.UpdateMapCamera
        previousMapDisplayConfigState = newState
    }

    fun updateMapCoreMainView(newState: MapCoreMainView){
        when(newState){
            MapCoreMainView.MAP -> {
                isPicturesShown.value = false
                isWebViewShown.value = false
                isMapShown.value = true
            }
            MapCoreMainView.WEB_VIEW -> {
                _state.value = ExperienceMapState.InitTransitionToPlayMap
            }
        }
    }

    fun callBackToPlayMap() {
        isMapShown.value = false
        isPicturesShown.value = false
        isWebViewShown.value = true
    }
}