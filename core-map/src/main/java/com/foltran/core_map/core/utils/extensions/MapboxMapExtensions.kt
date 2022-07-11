package com.foltran.core_map.core.utils.extensions

import android.content.Context
import android.os.Handler
import com.foltran.core_map.R
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_map.core.presentation.model.toEdgeInsets
import com.foltran.core_map.core.presentation.model.toEdgeInsetsZeroBase
import com.foltran.core_map.core.utils.getCameraPositionFromPolyline
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import kotlin.math.abs


fun MapboxMap.loadDarkSimpleStyle(context: Context, onStyleLoaded: (style: Style) -> Unit = {}) {
    loadStyleUri(context.getString(R.string.mapbox_style_uri)) {
        onStyleLoaded(it)
    }
}

data class CameraParams(
    val zoom: Double
)

fun MapboxMap.moveCameraToFitPolyline(mapCoreDisplayConfig: MapCoreDisplayConfig?): CameraParams? {

    if (mapCoreDisplayConfig == null) return null

    val polylineToFit = mapCoreDisplayConfig.focusCoordinates?.let {
        PolylineUtils.encode(it.map {Point.fromLngLat(it[1], it[0])}, 5)
    } ?: mapCoreDisplayConfig.mainPolyline

    val cameraParams = polylineToFit?.let {
        val camera = getCameraPositionFromPolyline(
            it,
            mapCoreDisplayConfig.padding.toEdgeInsets(),
            mapCoreDisplayConfig.bearing,
            mapCoreDisplayConfig.pitch
        )
        flyTo(
            camera,
            MapAnimationOptions.mapAnimationOptions {
                duration(400)
            }
        )
        camera
    }
    return CameraParams(cameraParams?.zoom ?: 13.0)
}

fun MapboxMap.moveCameraToStartOfPath(
    mapCoreDisplayConfig: MapCoreDisplayConfig?,
    mapCoreUIModel: MapCoreUIModel?,
    callBack: () -> Unit
) {

    val targetLon = mapCoreUIModel?.startLongitude
    val targetLat = mapCoreUIModel?.startLatitude

    if (targetLat == null || targetLon == null) return

    val coordinateDist : Double = cameraState.center.let {
        abs(targetLat - it.latitude()) + abs(targetLon - it.longitude())
    }
    val transitionTime: Long = (coordinateDist * 100000.0).coerceAtMost(400.0).toLong()

    mapCoreUIModel.let {
        mapCoreDisplayConfig?.let {

            flyTo(
                cameraOptions{
                    //center(Point.fromLngLat(mapCoreUIModel.startLongitude, mapCoreUIModel.startLatitude))
                    center(Point.fromLngLat(targetLon, targetLat))
                    zoom(13.0)
                    padding(mapCoreDisplayConfig.padding.toEdgeInsetsZeroBase())
                },
                MapAnimationOptions.mapAnimationOptions {
                    duration(transitionTime)
                }
            )
            Handler().postDelayed({
                callBack()
            }, transitionTime)
        }
    }
}
