package com.foltran.core_utils.extensions

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

private const val base64Prepend = "data:image/png;base64,"

fun Uri.toBase64(contentResolver: ContentResolver): String {

    val imageStream = contentResolver.openInputStream(this)
    val selectedImage = BitmapFactory.decodeStream(imageStream)

    val baos = ByteArrayOutputStream()
    selectedImage.compress(Bitmap.CompressFormat.JPEG, 50, baos)
    val b: ByteArray = baos.toByteArray()

    val base64 = Base64.encodeToString(b, Base64.DEFAULT)

    return base64Prepend + base64?.replace("\n", "")
}