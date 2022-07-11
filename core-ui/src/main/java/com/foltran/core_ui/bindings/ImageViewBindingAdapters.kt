package com.foltran.core_ui.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.foltran.core_utils.picasso.picassoImageTarget
import com.foltran.core_utils.picasso.retrieveImage
import com.squareup.picasso.Picasso

import android.graphics.BitmapFactory

import android.graphics.Bitmap
import com.foltran.core_ui.R
import com.squareup.picasso.Callback
import java.io.File


@BindingAdapter("drawableId")
fun ImageView.setImageResource(drawableId: Int?) {
    if (drawableId != null) setImageResource(drawableId)
}

@BindingAdapter("imageUrlSavedLocal")
fun ImageView.loadImageUrlSavedLocal(imageUrl: String?) {

    imageUrl?.let { url ->

        val path = imageUrl.replace("/", "_")

        val imgFile = File("/data/user/0/com.foltran.boost/app_" + path + "/my_image.jpeg")

        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
            setImageBitmap(myBitmap)
        } else {
            retrieveImage(context.applicationContext, path).let { file ->
                Picasso.with(context).load(url)
                    .into(picassoImageTarget(context.applicationContext, path, "my_image.jpeg"))
                Picasso.with(context).load(file).into(this)
            }
        }
    }
}

@BindingAdapter("imageUrl")
fun ImageView.loadImageUrl(imageUrl: String?) {

    if (imageUrl.isNullOrEmpty()) return

    Picasso.with(context)
        .load(imageUrl).also {
            context.getDrawable(R.drawable.ic_loader)?.let { drawable ->
                it.placeholder(drawable)
            }
        }
        .into(this)
}

fun ImageView.loadImageUrl(imageUrl: String?, onLoadedSuccessCallback: (() -> Unit)? = {}) {

    if (imageUrl.isNullOrEmpty()) return

    Picasso.with(context)
        .load(imageUrl).also {
            context.getDrawable(R.drawable.ic_loader)?.let { drawable ->
                it.placeholder(drawable)
            }
        }
        .into(this, object : Callback {
            override fun onSuccess() {
                onLoadedSuccessCallback?.invoke()
            }

            override fun onError() {

            }

        })
}
