package com.foltran.feature_experience.feed.presentation.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_experience.feed.presentation.model.ExperienceFeedItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.ClassCastException

class ExperienceFeedAdapter(val clickListener: ItemClickListener, val context: Context) :
    PagingDataAdapter<ExperienceFeedItem, RecyclerView.ViewHolder>(ExperienceItemDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitListInScope(data: PagingData<ExperienceItem>?) {

//        if (data == null) return
//
//        adapterScope.launch {
//            withContext(Dispatchers.Main) {
//                submitData(data)
//            }
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is ExperienceItemViewHolder -> holder.bind(getItem(position) as ExperienceFeedItem.ExperienceListItem, clickListener, context)
        }
    }

    override fun getItemViewType(position: Int) = when(getItem(position)){
        is ExperienceFeedItem.ExperienceListItem -> EXPERIENCE_ITEM_TYPE
        ExperienceFeedItem.LastItem -> LAST_ITEM_TYPE
        ExperienceFeedItem.FirstItem -> FIRST_ITEM_TYPE
        else -> LAST_ITEM_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        EXPERIENCE_ITEM_TYPE -> ExperienceItemViewHolder.from(parent)
        LAST_ITEM_TYPE -> LastItemViewHolder.from(parent)
        FIRST_ITEM_TYPE -> FirstItemViewHolder.from(parent)
        else -> throw ClassCastException("Unknown viewType: $viewType")
    }

    companion object {
        const val FIRST_ITEM_TYPE = -1
        const val LAST_ITEM_TYPE = 0
        const val EXPERIENCE_ITEM_TYPE = 1
    }
}
