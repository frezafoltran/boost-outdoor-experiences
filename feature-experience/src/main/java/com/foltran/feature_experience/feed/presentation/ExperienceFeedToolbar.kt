package com.foltran.feature_experience.feed.presentation

import androidx.lifecycle.MutableLiveData
import com.foltran.core_ui.components.FixedToolbarTypes
import com.foltran.core_ui.components.ToolbarComponentParams
import com.foltran.core_ui.components.ToolbarIcon
import com.foltran.feature_experience.R

fun getExperienceFeedToolbar(searchEditText: MutableLiveData<String>, onBackPressed: () -> Unit) =
    ToolbarComponentParams(
        editText = searchEditText,
        leftIcon = ToolbarIcon(
            onClick = { onBackPressed() }, hasBorder = false, drawableId = R.drawable.ic_back
        )
    )


fun getDefaultToolbar(onClickProfile: () -> Unit, onClickSearch: () -> Unit) =  ToolbarComponentParams(
    title = "Hi Joao!",
    rightIcon = ToolbarIcon(
        imageUrl = "https://i.ibb.co/HDqSSwV/John-Lennon-775x1024.jpg",
        onClick = {onClickProfile()},
        hasBorder = true,
        isScalable = true
    ),
    fixedToolbar = FixedToolbarTypes.FILTERS
)
