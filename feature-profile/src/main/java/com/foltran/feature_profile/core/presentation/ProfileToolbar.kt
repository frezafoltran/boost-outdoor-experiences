package com.foltran.feature_profile.core.presentation

import android.content.Context
import android.util.TypedValue
import com.foltran.core_ui.components.ToolbarAnimations
import com.foltran.core_ui.components.ToolbarComponentParams
import com.foltran.core_ui.components.ToolbarIcon
import com.foltran.core_ui.databinding.ComponentToolbarBinding
import com.foltran.core_utils.extensions.animateHeight
import com.foltran.core_utils.extensions.animateHeightByFactor
import com.foltran.feature_profile.R

fun getProfileFeedToolbar() =
    ToolbarComponentParams(
        titleByResId = R.string.navigation_profile,
        rightIcon = ToolbarIcon(
            hasBorder = true,
            imageUrl = "https://i.ibb.co/HDqSSwV/John-Lennon-775x1024.jpg",
            isScalable = true
        ),
        animations = object: ToolbarAnimations(){
            override fun animationEntering(context: Context, binding: ComponentToolbarBinding) {
                binding.toolbar.animateHeightByFactor(1.2)
            }

            override fun animationExiting(context: Context, binding: ComponentToolbarBinding) {
                val tv = TypedValue()
                if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                    val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
                    binding.toolbar.animateHeight(actionBarHeight)
                }
            }
        }
    )
