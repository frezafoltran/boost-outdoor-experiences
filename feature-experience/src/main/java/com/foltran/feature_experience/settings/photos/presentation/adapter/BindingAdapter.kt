package com.foltran.feature_experience.settings.photos.presentation.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter


@BindingAdapter("layout_constraintHorizontal_bias")
fun View.setLayoutConstraintGuideBegin(horizontalBias: Float) {
    val params = layoutParams as ConstraintLayout.LayoutParams
    params.horizontalBias = horizontalBias
    setLayoutParams(params)
}