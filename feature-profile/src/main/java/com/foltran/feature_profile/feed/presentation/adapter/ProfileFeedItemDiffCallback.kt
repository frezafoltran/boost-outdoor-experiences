package com.foltran.feature_profile.feed.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.foltran.core_experience.profile.model.ProfileFeedItem

class ProfileFeedItemDiffCallback : DiffUtil.ItemCallback<ProfileFeedItem>() {
    override fun areItemsTheSame(oldItem: ProfileFeedItem, newItem: ProfileFeedItem) = when {
        oldItem is ProfileFeedItem.ExperienceListItem && newItem is ProfileFeedItem.ExperienceListItem ->
            oldItem.item.id == newItem.item.id
        oldItem is ProfileFeedItem.Header && newItem is ProfileFeedItem.Header -> true
        else -> false
    }


    override fun areContentsTheSame(oldItem: ProfileFeedItem, newItem: ProfileFeedItem) =
        oldItem == newItem
}