package com.foltran.core_map.core.utils.extensions.settings

import android.content.Context
import android.view.View
import com.foltran.core_map.R
import com.foltran.core_map.core.presentation.model.MapCoreDisplayConfig
import com.foltran.core_map.core.presentation.model.MapCoreUIModel
import com.foltran.core_map.core.utils.extensions.*
import com.foltran.core_map.webview.Photo
import com.foltran.core_utils.extensions.bitmapFromDrawableRes
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*


class ExperienceSettingsPhotoManager(
    view: View,
    initialPhotos: List<Photo>,
    val mapCoreUIModel: MapCoreUIModel,
    val mapCoreDisplayConfig: MapCoreDisplayConfig,
    val context: Context,
    callbackOnCameraFinishMoving: (photo: Photo) -> Unit,
    callbackOnCameraMove: () -> Unit
) {

    private val mapView: MapView = view as MapView
    private val pointAnnotationManager: PointAnnotationManager = with(mapView) {

        val out = annotations.createPointAnnotationManager(
            this//,
//            annotationConfig = AnnotationConfig(
//                annotationSourceOptions = AnnotationSourceOptions(
//                    clusterOptions = ClusterOptions(
//                        cluster = false
//                    )
//                )
//            )
        )

        out.addClickListener(OnPointAnnotationClickListener {
            moveCameraToPoint(
                lat = it.point.latitude(),
                lon = it.point.longitude(),
                callBack = {
                    pointAnnotationIdToPhoto[it.id]?.let { photo ->
                        callbackOnCameraFinishMoving(photo)
                    }
                }
            )
            true
        })
        out
    }

    private val photoIdToPointAnnotation = HashMap<String, PointAnnotation>()
    private val pointAnnotationIdToPhoto = HashMap<Long, Photo>()

    val photos = ArrayList<Photo>()

    init {

        with(mapView) {

            addOnMapLoadedListener {
                updateMapCamera(mapCoreDisplayConfig)

                initialPhotos.forEach { photo ->
                    context.addPhotoAndAnnotation(photo)?.let {
                        photos.add(photo)
                        photoIdToPointAnnotation[photo.id] = it
                        pointAnnotationIdToPhoto[it.id] = photo
                        photo
                    }
                }
            }

            initMapStyle(context)
            onMapCameraMoveListener {
                callbackOnCameraMove()
            }
            updateMapPolyline(mapCoreUIModel)
        }
    }

    private fun Context.addPhotoAndAnnotation(photo: Photo): PointAnnotation? {

        return bitmapFromDrawableRes(
            this,
            R.drawable.ic_camera
        )?.let { bm ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(photo.lon, photo.lat))
                .withIconImage(bm)

            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    fun getCurrentCenter() = mapView.getCurrentCenter()

    fun addPhotoToCenter(experienceId: String): Photo? {

        val curCenter = mapView.getCurrentCenter()
        val photo = Photo(
            id = "${experienceId}_${curCenter[0]}_${curCenter[1]}",
            lat = curCenter[0],
            lon = curCenter[1],
            url = "https://i.ibb.co/sFrSLBh/IMG-20180516-120058.jpg",
            indexInStream = 0
        )
        return context.addPhotoAndAnnotation(photo)?.let {
            photos.add(photo)
            photoIdToPointAnnotation.put(photo.id, it)
            pointAnnotationIdToPhoto.put(it.id, photo)
            photo
        }
    }

    fun addPhoto(photo: Photo): Photo? {

        return context.addPhotoAndAnnotation(photo)?.let {
            photos.add(photo)
            photoIdToPointAnnotation.put(photo.id, it)
            pointAnnotationIdToPhoto.put(it.id, photo)
            photo
        }
    }

    fun deletePhoto(photo: Photo) {
        photoIdToPointAnnotation[photo.id]?.let {
            pointAnnotationManager.delete(it)
            photoIdToPointAnnotation.remove(photo.id)
            pointAnnotationIdToPhoto.remove(it.id)
        }
    }


}