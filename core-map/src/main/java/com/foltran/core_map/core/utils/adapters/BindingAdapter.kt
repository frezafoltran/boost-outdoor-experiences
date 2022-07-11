package com.foltran.core_map.core.utils.adapters

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.foltran.core_map.R
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCorePadding
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_map.core.utils.extensions.*
import com.google.android.material.tabs.TabLayout
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap

fun View.setupExperienceMap(mapCoreUIModel: MapCoreUIModel?) {

    with(this as MapView){
        defaultMapSettings()
        getMapboxMap().loadDarkSimpleStyle(context)
        addListOfPointsToMap(mapCoreUIModel)

        mapCoreUIModel?.let {
            addStartBeatleToMap(it.startLatitude, it.startLongitude)
        }
        //addStartBeatleToMap(mapCoreUIModel)

    }
}


fun View.setupMapCoreDisplayConfig(mapCoreDisplayConfig: MapCoreDisplayConfig?) {

    with (this as MapView) {
        with (getMapboxMap()){
            addOnMapLoadedListener{
                mapCoreDisplayConfig?.let{
                    moveCameraToFitPolyline(mapCoreDisplayConfig)
                }
            }
        }
    }
}

@BindingAdapter("hasMap", "mapCoreUIModel", "mapCoreDisplayConfig")
fun ConstraintLayout.setupExperienceMapContainer(hasMap: Boolean, mapCoreUIModel: MapCoreUIModel?, mapCoreDisplayConfig: MapCoreDisplayConfig?) {

    if (!hasMap){
        removeAllViews()
    }
    else {
        addView(MapView(context, MapInitOptions(context)))
        getChildAt(0).setupExperienceMap(mapCoreUIModel)
        getChildAt(0).setupMapCoreDisplayConfig(mapCoreDisplayConfig)
    }

}
