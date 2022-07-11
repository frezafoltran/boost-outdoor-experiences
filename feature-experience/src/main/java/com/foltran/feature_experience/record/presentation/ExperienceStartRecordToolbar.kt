package com.foltran.feature_experience.record.presentation

import com.foltran.core_ui.components.ToolbarComponentParams
import com.foltran.core_ui.components.ToolbarIcon
import com.foltran.feature_experience.R

fun getExperienceStartRecordToolbar() =
    ToolbarComponentParams(
        titleByResId = R.string.experience_start_record_toolbar,
        rightIcon = ToolbarIcon(
            onClick = { }, hasBorder = false, drawableId = R.drawable.ic_menu
        )
    )