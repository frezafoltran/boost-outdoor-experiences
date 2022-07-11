package com.foltran.core_ui.components


open class RoundImageComponentParams(
    open val drawableId: Int? = null,
    open val imageUrl: String? = null,
    open val onClick: (() -> Unit) = {},
    open val hasBorder: Boolean = false
) {
}
