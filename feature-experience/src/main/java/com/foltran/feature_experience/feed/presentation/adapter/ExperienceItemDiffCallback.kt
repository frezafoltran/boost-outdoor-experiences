package com.foltran.feature_experience.feed.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.foltran.core_experience.feed.presentation.model.ExperienceFeedItem

class ExperienceItemDiffCallback : DiffUtil.ItemCallback<ExperienceFeedItem>() {
    override fun areItemsTheSame(oldItem: ExperienceFeedItem, newItem: ExperienceFeedItem) = when {
        oldItem is ExperienceFeedItem.ExperienceListItem && newItem is ExperienceFeedItem.ExperienceListItem ->
            oldItem.item.id == newItem.item.id
        oldItem is ExperienceFeedItem.FirstItem && newItem is ExperienceFeedItem.FirstItem -> true
        oldItem is ExperienceFeedItem.LastItem && newItem is ExperienceFeedItem.LastItem -> true
        else -> false
    }


    override fun areContentsTheSame(oldItem: ExperienceFeedItem, newItem: ExperienceFeedItem) =
        oldItem == newItem
}