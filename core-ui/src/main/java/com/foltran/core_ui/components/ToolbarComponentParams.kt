package com.foltran.core_ui.components

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.lifecycle.MutableLiveData
import com.foltran.core_ui.databinding.ComponentToolbarBinding


data class ToolbarIcon(
    override val drawableId: Int? = null,
    override val imageUrl: String? = null,
    override val onClick: (() -> Unit) = {},
    override val hasBorder: Boolean = false,
    val isScalable: Boolean = false
) : RoundImageComponentParams()

open class ToolbarAnimations {
    open fun animationEntering(context: Context, binding: ComponentToolbarBinding){}
    open fun animationExiting(context: Context, binding: ComponentToolbarBinding){}
}

enum class FixedToolbarTypes {
    FILTERS
}

class ToolbarComponentParams(
    val title: String? = null,
    val titleByResId: Int? = null,
    var editText: MutableLiveData<String>? = null,
    val leftIcon: ToolbarIcon? = null,
    val rightIcon: ToolbarIcon? = null,
    val animations: ToolbarAnimations = ToolbarAnimations(),
    val fixedToolbar: FixedToolbarTypes? = null
){
    val hasLeftIcon = leftIcon != null
    val hasRightIcon = rightIcon != null
    val hasTitle = title != null || titleByResId != null
    val hasEditText = editText != null

    val hasFilterToolbar = fixedToolbar == FixedToolbarTypes.FILTERS

    fun getLeftRoundImageComponentParams() =
        if (hasLeftIcon) (leftIcon as  RoundImageComponentParams)
        else null

    fun getRightRoundImageComponentParams() =
        if (hasRightIcon)  (rightIcon as  RoundImageComponentParams)
        else null

    fun getTitle(context: Context) =
        if (titleByResId != null) context.getString(titleByResId)
        else title

}

