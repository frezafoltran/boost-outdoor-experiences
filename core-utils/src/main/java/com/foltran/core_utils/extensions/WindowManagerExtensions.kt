package com.foltran.core_utils.extensions

import android.util.DisplayMetrics
import android.view.WindowManager

fun WindowManager.getHeight(): Int {
    val displayMetrics = DisplayMetrics()
    defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun WindowManager.getWidth(): Int {
    val displayMetrics = DisplayMetrics()
    defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}