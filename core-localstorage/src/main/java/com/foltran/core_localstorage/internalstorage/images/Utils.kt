package com.foltran.core_localstorage.internalstorage.images

import android.content.Context
import android.graphics.Bitmap
import android.content.ContextWrapper
import java.lang.Exception
import android.R

import android.graphics.BitmapFactory
import java.io.*
import java.net.URL


const val EXPERIENCE_DIR = "experience_"

fun getFolderFromComplement(complement: String) = EXPERIENCE_DIR + complement

fun getBitmapFromUrl(url: String): Bitmap? = try {
    BitmapFactory.decodeStream(URL(url).openConnection().getInputStream())
} catch (e: IOException) {
    null
}

fun saveBitmapToInternalStorage(
    bitmap: Bitmap?,
    fileName: String,
    folderComplement: String,
    applicationContext: Context
): String? {

    return bitmap?.let {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir(getFolderFromComplement(folderComplement), Context.MODE_PRIVATE)
        val mypath = File(directory, fileName)
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }
}


fun saveToInternalStorage(url: String, fileName: String, folderComplement: String, applicationContext: Context): String? {

    return getBitmapFromUrl(url)?.let {
        val cw = ContextWrapper(applicationContext)
        val directory: File = cw.getDir(getFolderFromComplement(folderComplement), Context.MODE_PRIVATE)
        val mypath = File(directory, fileName)
        var fos: FileOutputStream? = null

        try {
            fos = FileOutputStream(mypath)
            it.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }
}

fun loadImageFromStorage(fileName: String, folderComplement: String): Bitmap? {
    return try {
        val path = "data/data/com.foltran.boost/app_${getFolderFromComplement(folderComplement)}"
        val f = File(path, fileName)
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        b
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    }
}

/*
 private fun updateLapStreams(experienceId: String) {

        repository.getExperienceLapStream(experienceId).onEach {
            when (it) {
                is Resource.Loading -> _state.value = ExperienceMenuState.Loading
                is Resource.Error -> _state.value = ExperienceMenuState.Error(it.message)
                is Resource.Success -> {

                    val coordinateStreams = it.data?.map { lapStream ->
                        lapStream.latLng
                    }
                    val lapIds = _experience.value?.laps?.map{ it.id}


                    viewModelScope.launch(Dispatchers.Main) {

                        val bitmaps = withContext(Dispatchers.IO){
                            val bitmaps = ArrayList<Bitmap>()
                            val bitmapForLaps = getStaticMapForLapsFromInternalStorage(
                                _experience.value?.id,
                                lapIds
                            )

                            //not all images are saved locally
                            if (lapIds?.size != bitmapForLaps?.size) {

                                lapIds?.forEachIndexed{ index, _ ->
                                    val url = getStaticMapForLaps(
                                        _experience.value?.map?.polyline,
                                        coordinateStreams,
                                        index
                                    )
                                    getBitmapFromUrl(url)?.let { bm ->
                                        bitmaps.add(bm)
                                    }
                                }
                                bitmaps
                            }
                            else {
                                bitmapForLaps?.map {
                                    it.bitmap
                                }
                            }
                        }
                        saveImageByUrlToInternalStorage.sendAction(
                            ImageToSave(
                                lapIds = lapIds,
                                experienceId = _experience.value?.id,
                                bitmaps = bitmaps
                            ))

                        updatePreviewExperienceLapsMapBitmap.sendAction(bitmaps!![0])
                        _state.value = ExperienceMenuState.Success

                    }

                    //val mapImageUrl = getStaticMapForLaps(_experience.value?.map?.polyline, coordinateStreams)

                    //updatePreviewExperienceLapsMapImageUrl.sendAction(mapImageUrl)
                    //_experience.value = it.data

                }
            }
        }.launchIn(viewModelScope)
    }
 */