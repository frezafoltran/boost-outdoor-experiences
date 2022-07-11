package com.foltran.core_utils.picasso

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.content.ContextWrapper
import android.util.Log
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.squareup.picasso.Target



fun picassoImageTarget(context: Context, imageDir: String, imageName: String): Target? {
    val cw = ContextWrapper(context)
    val directory: File =
        cw.getDir(imageDir, Context.MODE_PRIVATE) // path to /data/data/yourapp/app_imageDir
    return object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
            Thread {
                val myImageFile = File(directory, imageName) // Create image file
                var fos: FileOutputStream? = null
                try {
                    fos = FileOutputStream(myImageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    try {
                        fos?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath())
            }.start()
        }

        override fun onBitmapFailed(errorDrawable: Drawable?) {}
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            if (placeHolderDrawable != null) {
            }
        }
    }
}

fun retrieveImage(applicationContext: Context, dir: String): File {
    val cw = ContextWrapper(applicationContext)
    val directory = cw.getDir(dir, Context.MODE_PRIVATE)
    return File(directory, "my_image.jpeg")
}