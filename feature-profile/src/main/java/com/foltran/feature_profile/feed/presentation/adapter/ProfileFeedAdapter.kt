package com.foltran.feature_profile.feed.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_experience.profile.model.ProfileFeedItem

class ProfileFeedAdapter(
    private val recyclerView: GridRecyclerView,
    private val clickListener: ItemClickListener,
    private val context: Context
) : PagingDataAdapter<ProfileFeedItem, RecyclerView.ViewHolder>(ProfileFeedItemDiffCallback()) {

    private var currentSpan = SPAN_ZOOM_IN
    private var profileItemViewHolderLabelType =
        ProfileFeedItemViewHolder.Companion.LabelTypes.DATE_MONTH_DAY

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItem(position) != null) {
            when (holder) {
                is ProfileFeedItemViewHolder -> holder.bind(
                    getItem(position) as ProfileFeedItem.ExperienceListItem,
                    clickListener,
                    profileItemViewHolderLabelType
                )
                is ProfileFeedHeaderViewHolder -> holder.bind(getItem(position) as ProfileFeedItem.Header)
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ProfileFeedItem.ExperienceListItem -> EXPERIENCE_ITEM_TYPE
        is ProfileFeedItem.Header -> LAST_ITEM_TYPE
        ProfileFeedItem.Footer -> FOOTER_ITEM_TYPE
        else -> LAST_ITEM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        EXPERIENCE_ITEM_TYPE -> ProfileFeedItemViewHolder.from(parent)
        LAST_ITEM_TYPE -> ProfileFeedHeaderViewHolder.from(parent)
        FOOTER_ITEM_TYPE -> ProfileFeedFooterViewHolder.from(parent)
        else -> throw ClassCastException("Unknown viewType: $viewType")
    }

    suspend fun submitDataAndUpdateLabelType(
        pagingData: PagingData<ProfileFeedItem>,
        labelType: ProfileFeedItemViewHolder.Companion.LabelTypes
    ) {
        //recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, 0)
        recyclerView.smoothScrollToPosition(0)
        //(recyclerView.layoutManager as GridLayoutManager?)?.scrollToPosition(0)
        profileItemViewHolderLabelType = labelType
        submitData(pagingData)
        notifyAnimationRangeChanged()
    }

    val scalableSpanSizeLookUp: GridLayoutManager.SpanSizeLookup =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (getItemViewType(position) != EXPERIENCE_ITEM_TYPE) TOTAL_SPAN else currentSpan
            }
        }

    fun toggleColumns(): Int {
        currentSpan = if (currentSpan == SPAN_ZOOM_IN) SPAN_ZOOM_OUT else SPAN_ZOOM_IN
        notifyAnimationRangeChanged()
        return currentSpan
    }

    private fun notifyAnimationRangeChanged() {
        notifyItemRangeChanged(0, itemCount)
    }


    companion object {
        const val LAST_ITEM_TYPE = 0
        const val EXPERIENCE_ITEM_TYPE = 1
        const val FOOTER_ITEM_TYPE = 2

        const val TOTAL_SPAN = 6
        const val SPAN_ZOOM_IN = (TOTAL_SPAN.toDouble() / 2.0).toInt()
        const val SPAN_ZOOM_OUT = (TOTAL_SPAN.toDouble() / 3.0).toInt()
    }
}
