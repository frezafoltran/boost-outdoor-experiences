package com.foltran.core_map.core.utils.extensions.settings

import android.os.Handler
import android.view.View
import com.foltran.core_map.R
import com.foltran.core_map.webview.Photo
import com.foltran.core_utils.extensions.bitmapFromDrawableRes
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlin.math.abs


data class PointAnnotationSimplified(
    val coordinates: List<Double>
)

fun PointAnnotation.toPointAnnotationSimplified() = PointAnnotationSimplified(
    coordinates = listOf(point.latitude(), point.longitude())
)

fun MapView.addPictureBeatlesToMap(
    photos: List<Photo>?,
    onClickPictureListener: (pointAnnotationSimplified: PointAnnotationSimplified) -> Boolean
) {

    photos?.let {
        bitmapFromDrawableRes(
            context,
            R.drawable.ic_camera
        )?.let { bm ->
            val pointAnnotationManager = annotations.createPointAnnotationManager(this)

            pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
                onClickPictureListener(it.toPointAnnotationSimplified())
            })

            for (picture in photos) {

                if (picture.lat != null && picture.lon != null) {
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(Point.fromLngLat(picture.lon, picture.lat))
                        .withIconImage(bm)
                    pointAnnotationManager.create(pointAnnotationOptions)
                }
            }
        }
    }
}

fun MapView.addSinglePhotoToMap(
    photo: Photo?,
    onClickPictureListener: (pointAnnotationSimplified: PointAnnotationSimplified) -> Boolean
) {

    photo?.let {
        bitmapFromDrawableRes(
            context,
            R.drawable.ic_camera
        )?.let { bm ->
            val pointAnnotationManager = annotations.createPointAnnotationManager(this)



            pointAnnotationManager.addClickListener(OnPointAnnotationClickListener {
                onClickPictureListener(it.toPointAnnotationSimplified())
            })

            if (photo.lat != null && photo.lon != null) {
                val pointAnnotationOptions = PointAnnotationOptions()
                    .withPoint(Point.fromLngLat(photo.lon, photo.lat))
                    .withIconImage(bm)
                pointAnnotationManager.create(pointAnnotationOptions)
            }

        }
    }
}

fun MapView.removePhoto(
    photo: Photo?,
) {

    photo?.let {
        val retrieveAnnotationManager = annotations.createPointAnnotationManager(this)

        //retrieveAnnotationManager.delete()
        annotations.removeAnnotationManager(retrieveAnnotationManager)
    }
}

fun View.updateMapPhotos(
    photos: List<Photo>?,
    onClickPictureListener: (pointAnnotationSimplified: PointAnnotationSimplified) -> Boolean
) {
    with (this as MapView) {
        addPictureBeatlesToMap(photos){
            onClickPictureListener(it)
        }
    }
}

fun View.addPhotoToMap(
    photo: Photo?,
    onClickPictureListener: (pointAnnotationSimplified: PointAnnotationSimplified) -> Boolean
) {
    with (this as MapView) {
        addSinglePhotoToMap(photo){
            onClickPictureListener(it)
        }
    }
}


fun MapboxMap.moveCameraToPoint(
    lat: Double,
    lon: Double,
    callBack: () -> Unit
) {
    val coordinateDist : Double = cameraState.center.let {
        abs(lat - it.latitude()) + abs(lon - it.longitude())
    }
    val transitionTime: Long = (coordinateDist * 100000.0).coerceAtMost(200.0).toLong()
    flyTo(
        cameraOptions{
            center(Point.fromLngLat(lon, lat))
            zoom(13.0)
        },
        MapAnimationOptions.mapAnimationOptions {
            duration(transitionTime)
        }
    )

    Handler().postDelayed({
        callBack()
    }, if (transitionTime > 0) transitionTime + 20 else 0)
}

fun View.moveCameraToPoint(lat: Double?, lon: Double?,  callBack: () -> Unit) {
    with (this as MapView) {
        if (lat != null && lon != null) {
            getMapboxMap().moveCameraToPoint(lat, lon, callBack)
        }
     }
}
