package com.foltran.feature_experience.feed.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.foltran.core_experience.feed.presentation.model.ExperienceFeedItem
import com.foltran.feature_experience.R
import com.foltran.feature_experience.databinding.ItemExperienceFeedBinding
import com.foltran.feature_experience.databinding.ItemExperienceFeedBottomBinding
import com.foltran.feature_experience.databinding.ItemExperienceFeedTopBinding
import com.foltran.feature_experience.feed.presentation.toUIModel
import com.google.android.material.tabs.TabLayout


class ExperienceItemViewHolder private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ExperienceFeedItem.ExperienceListItem, clickListener: ItemClickListener, context: Context) {

        when(binding){
            is ItemExperienceFeedBinding -> {
                binding.itemPosition = layoutPosition
                binding.item = item.toUIModel(context)
                binding.clickListener = clickListener
            }
        }


        val viewPager = binding.root.findViewById<ViewPager>(R.id.viewPagerMain)

        binding.root.findViewById<TabLayout>(R.id.tabDots).setupWithViewPager(viewPager, true)
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ExperienceItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return ExperienceItemViewHolder(
                ItemExperienceFeedBinding.inflate(inflater, parent, false)
            )
        }
    }
}

class LastItemViewHolder private constructor(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): LastItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return LastItemViewHolder(
                ItemExperienceFeedBottomBinding.inflate(inflater, parent, false)
            )
        }
    }
}

class FirstItemViewHolder private constructor(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): FirstItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return FirstItemViewHolder(
                ItemExperienceFeedTopBinding.inflate(inflater, parent, false)
            )
        }
    }
}
