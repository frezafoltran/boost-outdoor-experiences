package com.foltran.feature_experience.map.presentation.adapters

import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import com.foltran.feature_experience.R
import java.io.IOException


val tempVideoUrl = "TODO"

@Throws(Throwable::class)
fun retriveVideoFrameFromVideo(videoPath: String?): Bitmap? {
    var bitmap: Bitmap? = null
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
            videoPath,
            HashMap<String, String>()
        ) else mediaMetadataRetriever.setDataSource(videoPath)
        bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
    } catch (e: Exception) {
        e.printStackTrace()
        throw Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.message)
    } finally {
        if (mediaMetadataRetriever != null) {
            mediaMetadataRetriever.release()
        }
    }
    return bitmap
}
