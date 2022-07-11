package com.foltran.feature_profile.feed.presentation.adapter

import android.R.attr.maxHeight
import android.R.attr.maxWidth
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GridRecyclerView : RecyclerView {

    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        if (!isInEditMode) if (layout is GridLayoutManager) {
            super.setLayoutManager(layout)
        } else {
            throw ClassCastException("You should only use a GridLayoutManager with GridRecyclerView.")
        }
    }

    override fun attachLayoutAnimationParameters(
        child: View?,
        params: ViewGroup.LayoutParams,
        index: Int,
        count: Int
    ) {
        if (!isInEditMode) {
            val adapter = adapter
            val layoutManager = layoutManager
            if (adapter != null && layoutManager is GridLayoutManager) {
                val animationParams = params.layoutAnimationParameters
                        as? GridLayoutAnimationController.AnimationParameters
                    ?: GridLayoutAnimationController.AnimationParameters()
                params.layoutAnimationParameters = animationParams

                val columns = layoutManager.spanCount
                animationParams.count = count
                animationParams.index = index
                animationParams.columnsCount = columns
                animationParams.rowsCount = count / columns

                val invertedIndex = count - 1 - index
                animationParams.column = columns - 1 - (invertedIndex % columns)
                animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns
            } else {
                super.attachLayoutAnimationParameters(child, params, index, count)
            }
        }
    }
}