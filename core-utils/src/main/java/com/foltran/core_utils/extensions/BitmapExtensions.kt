package com.foltran.core_utils.extensions

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.foltran.core_utils.R
import java.io.ByteArrayOutputStream

private const val base64Prepend = "data:image/png;base64,"

fun Bitmap.addWhiteBorder(borderSize: Float = 10f): Bitmap {
    val bmpWithBorder: Bitmap = Bitmap.createBitmap(
        (width.toFloat() + borderSize * 2f).toInt(),
        (height.toFloat() + borderSize * 2f).toInt(),
        config
    )
    val canvas = Canvas(bmpWithBorder)
    canvas.drawColor(Color.WHITE)
    canvas.drawBitmap(this, borderSize, borderSize, null)
    return bmpWithBorder
}

private fun Bitmap.makeSquare(): Bitmap {

    val diff = height - width

    return if (diff > 0) {
        val targetSize = width
        Bitmap.createBitmap(this, 0, diff / 2, targetSize, targetSize)
    } else if (diff < 0) {
        val targetSize = height
        Bitmap.createBitmap(this, diff / 2, 0, targetSize, targetSize)
    } else {
        this
    }
}

fun Bitmap.buildVideoWrapper(context: Context): Bitmap {
    val paintWidthSize = 20f
    val paintWidthSizeOffset = 25f
    val borderRadius = 20f
    with(this.makeSquare()) {
        val desiredSizeVideoIcon = height.toDouble() / 2.0

        val bmpWithDecoration: Bitmap = Bitmap.createBitmap(
            (width.toFloat() + paintWidthSizeOffset * 2f).toInt(),
            (height.toFloat() + paintWidthSizeOffset * 2f).toInt(),
            config
        )
        val canvas = Canvas(bmpWithDecoration)
        canvas.drawRoundRect(
            RectF(
                0f,
                0f,
                bmpWithDecoration.width.toFloat(),
                bmpWithDecoration.height.toFloat()
            ),
            borderRadius,
            borderRadius,
            Paint().apply {
                color = Color.WHITE
                strokeWidth = paintWidthSize
                style = Paint.Style.FILL
            }
        )
        //canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(this, paintWidthSizeOffset, paintWidthSizeOffset, null)

        bitmapFromDrawableRes(context, R.drawable.ic_video_white)?.getResizedBitmap(
            desiredSizeVideoIcon.toInt(),
            desiredSizeVideoIcon.toInt()
        )?.let { videoBitmap ->
            canvas.drawBitmap(
                videoBitmap,
                paintWidthSizeOffset + (width - videoBitmap.width) / 2,
                paintWidthSizeOffset + (height - videoBitmap.height) / 2,
                null
            )
        }

        return bmpWithDecoration
    }
}

fun Bitmap.getResizedBitmap(newWidth: Int, newHeight: Int): Bitmap? {
    val width = width
    val height = height
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)

    val resizedBitmap = Bitmap.createBitmap(
        this, 0, 0, width, height, matrix, false
    )
    recycle()
    return resizedBitmap
}

fun Bitmap.toBase64(): String {

    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val b: ByteArray = baos.toByteArray()

    val base64 = Base64.encodeToString(b, Base64.DEFAULT)

    return base64Prepend + base64?.replace("\n", "")
}

fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int): Bitmap? =
    convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}