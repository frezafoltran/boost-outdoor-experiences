package com.foltran.core_map.core.utils.extensions.photos

import android.content.Context
import android.view.View
import com.foltran.core_map.R
import com.foltran.core_map.core.utils.extensions.addOnMapLoadedListener
import com.foltran.core_map.core.utils.extensions.settings.moveCameraToPoint
import com.foltran.core_map.webview.Photo
import com.foltran.core_utils.extensions.bitmapFromDrawableRes
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*

/**
 * Manages adding photos to map and passing callback to Activity/Fragment that invokes it
 */
class ExperiencePhotoManager(
    view: View,
    photos: List<Photo>,
    callbackOnPhotoClicked: (photo: Photo) -> Unit,
    val context: Context,
) {

    private val mapView: MapView = view as MapView
    private val pointAnnotationManager: PointAnnotationManager = with(mapView) {

        val out = annotations.createPointAnnotationManager(this)

        out.addClickListener(OnPointAnnotationClickListener {
            moveCameraToPoint(
                lat = it.point.latitude(),
                lon = it.point.longitude(),
                callBack = {
                    pointAnnotationIdToPhoto[it.id]?.let { photo ->
                        callbackOnPhotoClicked(photo)
                    }
                }
            )
            true
        })
        out
    }

    private val photoIdToPointAnnotation = HashMap<String, PointAnnotation>()
    private val pointAnnotationIdToPhoto = HashMap<Long, Photo>()

    init {

        with(mapView) {

            addOnMapLoadedListener {
                photos.forEach { photo ->
                    context.addPhotoAndAnnotation(photo)?.let {
                        photoIdToPointAnnotation[photo.id] = it
                        pointAnnotationIdToPhoto[it.id] = photo
                    }
                }
            }
        }
    }


    private fun Context.addPhotoAndAnnotation(photo: Photo): PointAnnotation? {

        return bitmapFromDrawableRes(
            this,
            R.drawable.ic_camera_round
        )?.let { bm ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(photo.lon, photo.lat))
                .withIconImage(bm)

            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }
}
