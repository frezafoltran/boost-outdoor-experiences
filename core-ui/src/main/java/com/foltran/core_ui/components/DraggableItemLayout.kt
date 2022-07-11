package com.foltran.core_ui.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.foltran.core_ui.R

@BindingAdapter("isCollapsed")
fun DraggableItemLayout.setScalableHeight(isCollapsed: Boolean?){
    if (isCollapsed == true) {
        layoutParams.height = 500
    }
    else {
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}

class DraggableItemLayout : ConstraintLayout {

    var isDraggable: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.i("JVFF", "intercept, " + isDraggable)
        return isDraggable
    }

    fun setIsDraggable(value: Boolean) {
        setBackgroundDrawable(if (value) resources.getDrawable(R.drawable.border_draggable_item) else null)
        this.isDraggable = value
    }
}