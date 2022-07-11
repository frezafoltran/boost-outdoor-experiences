package com.foltran.core_utils.extensions

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.hexFromColor(colorRes: Int) =
    "#" + Integer.toHexString(ContextCompat.getColor(this, colorRes))
