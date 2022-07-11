package com.foltran.core_map.core.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_map.core.utils.style.defaultLineColor
import com.foltran.core_map.core.utils.style.defaultLineWidth
import com.foltran.core_map.core.utils.style.highlightLineColor
import com.foltran.core_map.core.utils.style.highlightLineWidth
import com.foltran.core_utils.extensions.bitmapFromDrawableRes
import com.foltran.core_utils.extensions.buildVideoWrapper
import com.mapbox.geojson.Point
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.scalebar.scalebar

fun MapView.defaultMapSettings() {
    scalebar.updateSettings {
        enabled = false
    }
    compass.updateSettings {
        enabled = false
    }
}

fun MapView.addListOfPointsToMap(mapCoreUI: MapCoreUIModel?): PolylineAnnotationOptions? =
    mapCoreUI?.polyline?.let {

        val points = PolylineUtils.decode(mapCoreUI.polyline, 5)

        val polylineAnnotationManager = annotations.createPolylineAnnotationManager(this)
        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withLineColor(defaultLineColor)
            .withLineWidth(defaultLineWidth)
            .withPoints(points)

        polylineAnnotationManager?.create(polylineAnnotationOptions)
        polylineAnnotationOptions
    }

fun MapView.addListOfFocusedCoordinatesToMap(mapCoreUI: MapCoreUIModel?): PolylineAnnotationOptions? =
    mapCoreUI?.focusCoordinates?.let {

        val points = it.map {  coord ->
            Point.fromLngLat(coord[1], coord[0])
        }

        val polylineAnnotationManager = annotations.createPolylineAnnotationManager(this)
        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withLineColor("#F6931D")
            .withLineWidth(6.0)
            .withPoints(points)

        polylineAnnotationManager?.create(polylineAnnotationOptions)
        polylineAnnotationOptions
    }


fun View.initMapStyle(context: Context) {
    with (this as MapView) {

        defaultMapSettings()
        getMapboxMap().loadDarkSimpleStyle(context)
    }
}

fun View.updateMapPolyline(mapCoreUIModelObservable: MapCoreUIModel?) {
    with (this as MapView) {
        addListOfPointsToMap(mapCoreUIModelObservable)
    }
}

fun View.updateMapFocusCoordinatesPolyline(mapCoreUIModelObservable: MapCoreUIModel?) {
    with (this as MapView) {
        addListOfFocusedCoordinatesToMap(mapCoreUIModelObservable)
    }
}


fun View.updateMapCamera(mapCoreDisplayConfig: MapCoreDisplayConfig?): CameraParams? {
    with (this as MapView){
        return getMapboxMap().moveCameraToFitPolyline(mapCoreDisplayConfig)
    }
}

fun View.moveCameraToStartOfPath(
    mapCoreUIModelObservable: MapCoreUIModel?,
    mapCoreDisplayConfig: MapCoreDisplayConfig?,
    callbackToPlayMap: () -> Unit
) {
    with (this as MapView) {
        getMapboxMap().moveCameraToStartOfPath(
            mapCoreDisplayConfig,
            mapCoreUIModelObservable
        ) {
            callbackToPlayMap()
        }
    }
}

fun View.addOnMapLoadedListener(callBack: () -> Unit) {
    with (this as MapView) {
        getMapboxMap().addOnMapLoadedListener {
            callBack()
        }
    }
}

fun View.getCurrentCenter(): List<Double> {
    with (this as MapView) {

        return getMapboxMap().cameraState.center.let {
            listOf(it.latitude(), it.longitude())
        }
    }
}

fun View.onMapCameraMoveListener(onCameraChange: () -> Unit) {
    with(this as MapView) {
        getMapboxMap().addOnCameraChangeListener {
            onCameraChange()
        }
    }
}

// ================== NEW Stuff

internal fun MapView.addBitMapToMap(latitude: Double, longitude: Double, bitmap: Bitmap) {

    val pointAnnotationManager = annotations.createPointAnnotationManager(this)
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(longitude, latitude))
        .withIconImage(bitmap)
        .withIconSize(0.15)

    pointAnnotationManager.create(pointAnnotationOptions)

}

internal fun MapView.addIconToMap(latitude: Double, longitude: Double, drawableId: Int) {

    bitmapFromDrawableRes(
        context,
        drawableId
    )?.let { bm ->
        val pointAnnotationManager = annotations.createPointAnnotationManager(this)
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(Point.fromLngLat(longitude, latitude))
            .withIconImage(bm)
        pointAnnotationManager.create(pointAnnotationOptions)
    }
}

/**
 * coordinateSegments items are in <lat, lon> order
 */
internal fun MapView.addListOfCoordinatesToMap(coordinateSegments: List<List<List<Double>>>?) {

    val polylineAnnotationManager = annotations.createPolylineAnnotationManager(this)
    coordinateSegments?.forEach {

        val polylineAnnotationOptions: PolylineAnnotationOptions = PolylineAnnotationOptions()
            .withLineColor(highlightLineColor)
            .withLineWidth(highlightLineWidth)
            .withPoints(it.map { elem -> Point.fromLngLat(elem[1], elem[0])})

        polylineAnnotationManager.create(polylineAnnotationOptions)
    }

}
