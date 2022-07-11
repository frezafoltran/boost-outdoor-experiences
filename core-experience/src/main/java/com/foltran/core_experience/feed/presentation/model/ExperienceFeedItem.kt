package com.foltran.core_experience.feed.presentation.model

import com.foltran.core_experience.shared.data.model.ExperienceItem


sealed class ExperienceFeedItem {

    data class ExperienceListItem(
        val item: ExperienceItem
    ): ExperienceFeedItem()

    object LastItem: ExperienceFeedItem()

    object FirstItem: ExperienceFeedItem()
}