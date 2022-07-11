package com.foltran.core_ui.bindings

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

private const val START = 0
private const val TOP = 1
private const val END = 2
private const val BOTTOM = 3

fun TextView.setDrawables(@DrawableRes start: Int? = 0, @DrawableRes top:Int? = 0, @DrawableRes end:Int? = 0, @DrawableRes bottom:Int? = 0) {
    val drawableStart = start?.let { if (start == 0) compoundDrawables[START] else ContextCompat.getDrawable(context, start) }
    val drawableTop = top?.let { if (top == 0) compoundDrawables[TOP] else ContextCompat.getDrawable(context, top) }
    val drawableEnd = end?.let { if (end == 0) compoundDrawables[END] else ContextCompat.getDrawable(context, end) }
    val drawableBottom = bottom?.let { if (bottom == 0) compoundDrawables[BOTTOM] else ContextCompat.getDrawable(context, bottom) }
    setCompoundDrawablesWithIntrinsicBounds(drawableStart, drawableTop, drawableEnd, drawableBottom)
}

@BindingAdapter("drawableStart")
fun TextView.drawableStart(@DrawableRes drawableStart: Int?) {
    setDrawables(start = drawableStart)
}

@BindingAdapter("drawableTop")
fun TextView.drawableTop(@DrawableRes drawableTop: Int?) {
    setDrawables(top = drawableTop)
}

@BindingAdapter("drawableEnd")
fun TextView.drawableEnd(@DrawableRes drawableEnd: Int?) {
    setDrawables(end = drawableEnd)
}

@BindingAdapter("drawableBottom")
fun TextView.drawableBottom(@DrawableRes drawableBottom: Int?) {
    setDrawables(bottom = drawableBottom)
}