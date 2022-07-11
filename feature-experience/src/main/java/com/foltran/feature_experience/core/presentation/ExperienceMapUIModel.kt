package com.foltran.feature_experience.core.presentation

import androidx.lifecycle.MutableLiveData
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCorePadding
import com.google.android.material.bottomsheet.BottomSheetBehavior

interface ExperienceMapUIModel {

    val mapDisplayConfigObservable : MutableLiveData<MapCoreDisplayConfig>
    val mapDisplayConfigBottomObservable : MutableLiveData<MapCoreDisplayConfig>

    fun setMapDisplayConfigObservableMainPolyline(mainPolyline: String)

    fun updateDisplayConfigOnBottomSheetChange(
        newState: Int = BottomSheetBehavior.STATE_HALF_EXPANDED,
        screenHeight: Double
    )

    fun setMapDisplayConfigFocusCoordinates(coordinates: List<List<Double>>?)
}

class ExperienceMapUIModelImpl: ExperienceMapUIModel {

    override val mapDisplayConfigObservable = MutableLiveData(MapCoreDisplayConfig())
    override val mapDisplayConfigBottomObservable = MutableLiveData(MapCoreDisplayConfig())

    override fun setMapDisplayConfigObservableMainPolyline(mainPolyline: String) {
        mapDisplayConfigObservable.value = mapDisplayConfigObservable.value?.copy(
            mainPolyline = mainPolyline
        )
        mapDisplayConfigBottomObservable.value = mapDisplayConfigBottomObservable.value?.copy(
            mainPolyline = mainPolyline
        )
    }

    override fun updateDisplayConfigOnBottomSheetChange(newState: Int, screenHeight: Double) {

        val newDisplayConfig = when (newState) {
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                MapCoreDisplayConfig(
                    mainPolyline = mapDisplayConfigObservable.value?.mainPolyline,
                    focusCoordinates = mapDisplayConfigObservable.value?.focusCoordinates,
                    padding = MapCorePadding(bottom = screenHeight * 0.53)
                )
            }
            BottomSheetBehavior.STATE_COLLAPSED ->
                MapCoreDisplayConfig(
                    mainPolyline = mapDisplayConfigObservable.value?.mainPolyline,
                    focusCoordinates = mapDisplayConfigObservable.value?.focusCoordinates,
                    bearing = 5.0,
                    pitch = 30.0
                )
            else -> null
        }

        newDisplayConfig?.let{
            mapDisplayConfigObservable.value = it
        }
    }

    override fun setMapDisplayConfigFocusCoordinates(coordinates: List<List<Double>>?) {

        mapDisplayConfigObservable.value = mapDisplayConfigObservable.value?.copy(
            focusCoordinates = coordinates
        )
        mapDisplayConfigBottomObservable.value = mapDisplayConfigBottomObservable.value?.copy(
            focusCoordinates = coordinates
        )
    }
}