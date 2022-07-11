package com.foltran.feature_profile.feed.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.foltran.core_experience.profile.model.ProfileFeedItem
import com.foltran.core_map.images.utils.getStaticMapCleanStyle
import com.foltran.core_ui.bindings.loadImageUrl
import com.foltran.core_utils.date.formatUTCDateToDayMonth
import com.foltran.core_utils.date.formatUTCDateToDayMonthYear
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.feature_profile.databinding.FooterProfileFeedBinding
import com.foltran.feature_profile.databinding.HeaderDateProfileFeedBinding
import com.foltran.feature_profile.databinding.ItemProfileFeedBinding


class ProfileFeedItemViewHolder private constructor(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: ProfileFeedItem.ExperienceListItem,
        clickListener: ItemClickListener,
        labelType: LabelTypes
    ) {

        when(binding){
            is ItemProfileFeedBinding -> {
                val polyline = item.item.map.summaryPolyline ?: item.item.map.polyline ?: ""
                val mapUrl = getStaticMapCleanStyle(polyline)
                binding.imageViewMap.loadImageUrl(mapUrl)
                binding.clickListener = clickListener

                binding.imageViewLabel.text = when(labelType){
                    LabelTypes.DATE_MONTH_DAY -> item.item.startDate.formatUTCDateToDayMonth()
                    LabelTypes.DATE_MONTH_DAY_YEAR -> item.item.startDate.formatUTCDateToDayMonthYear()
                    LabelTypes.DISTANCE -> item.item.distance.meterToKm()
                }
            }
        }

        binding.executePendingBindings()
    }

    companion object {

        enum class LabelTypes {
            DATE_MONTH_DAY,
            DATE_MONTH_DAY_YEAR,
            DISTANCE
        }

        fun from(parent: ViewGroup): ProfileFeedItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return ProfileFeedItemViewHolder(
                ItemProfileFeedBinding.inflate(inflater, parent, false)
            )
        }
    }
}

class ProfileFeedHeaderViewHolder private constructor(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileFeedItem.Header) {

        when(binding){
            is HeaderDateProfileFeedBinding -> {
                binding.label.text = item.date
                binding.executePendingBindings()
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ProfileFeedHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return ProfileFeedHeaderViewHolder(
                HeaderDateProfileFeedBinding.inflate(inflater, parent, false)
            )
        }
    }
}

class ProfileFeedFooterViewHolder private constructor(private val binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): ProfileFeedFooterViewHolder {
            val inflater = LayoutInflater.from(parent.context)

            return ProfileFeedFooterViewHolder(
                FooterProfileFeedBinding.inflate(inflater, parent, false)
            )
        }
    }
}


