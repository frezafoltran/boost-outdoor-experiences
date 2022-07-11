package com.foltran.feature_profile.feed.presentation

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_experience.profile.model.ProfileFeedItem
import com.foltran.core_utils.bases.BaseViewModel
import com.foltran.core_utils.observable.ActionLiveData
import com.foltran.feature_profile.feed.presentation.adapter.ItemClickListener
import com.foltran.feature_profile.feed.presentation.adapter.ProfileFeedAdapter
import java.lang.ClassCastException

class ProfileFeedViewModel : BaseViewModel(), ItemClickListener {

    private val _toggleFeedZoomEvent = ActionLiveData<Unit>()
    val toggleFeedZoomEvent: LiveData<Unit> = _toggleFeedZoomEvent

    override fun onClickItem(experienceId: String) {
        // TODO
    }

    fun onClickToggleFeedZoom() {
        _toggleFeedZoomEvent.postValue(Unit)
    }

    fun onFeedScrolledUpdateStickyHeader(recyclerView: RecyclerView): String? {

        val layoutManager: GridLayoutManager = recyclerView.layoutManager as GridLayoutManager
        val adapter = recyclerView.adapter as ProfileFeedAdapter

        val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        val firstVisibleHeaderPosition =
            (firstVisiblePosition..lastVisiblePosition).firstOrNull {
                adapter.getItemViewType(it) == ProfileFeedAdapter.LAST_ITEM_TYPE
            } ?: -1
        val firstNotVisibleHeaderPosition =
            (firstVisiblePosition - 1 downTo 0).firstOrNull {
                adapter.getItemViewType(it) == ProfileFeedAdapter.LAST_ITEM_TYPE
            } ?: -1

        val stickyHeaderPosition =
            layoutManager.getChildAt(firstVisibleHeaderPosition - firstVisiblePosition)?.let {
                if (it.y < 200) -1 else null
            } ?: firstNotVisibleHeaderPosition

        return if (stickyHeaderPosition != -1) {
            try {
                (adapter.snapshot().items[stickyHeaderPosition] as ProfileFeedItem.Header).date
            } catch (e: ClassCastException) {
                null
            }
        } else
            null
    }

}