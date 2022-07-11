package com.foltran.feature_experience.feed.presentation.adapter

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.foltran.feature_experience.feed.presentation.ExperienceFeedItemViewPagerAdapter
import com.foltran.feature_experience.feed.presentation.ItemUI


@BindingAdapter("imageUrls", "item", "clickListener", "previewMap", "rvPosition")
internal fun ViewPager.setSampleViewPager(
    imageUrls: List<String>,
    item: ItemUI,
    clickListener: ItemClickListener,
    previewMap: String,
    rvPosition: Int
) {
    adapter = ExperienceFeedItemViewPagerAdapter(
        context,
        imageUrls,
        item,
        clickListener,
        previewMap,
        rvPosition
    )
}
