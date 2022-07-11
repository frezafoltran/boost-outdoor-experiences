package com.foltran.feature_experience.feed.presentation.adapter

import com.foltran.feature_experience.feed.presentation.ItemUI

interface ItemClickListener {
    fun onClickItem(item: ItemUI, position: Int)
}