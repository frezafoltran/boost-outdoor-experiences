package com.foltran.core_experience.profile.model

import com.foltran.core_experience.shared.data.model.ExperienceItem


sealed class ProfileFeedItem {

    data class ExperienceListItem(
        val item: ExperienceItem
    ): ProfileFeedItem()

    data class Header(val date: String): ProfileFeedItem()

    object Footer: ProfileFeedItem()

}