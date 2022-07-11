package com.foltran.feature_experience.feed.presentation.listener

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller


fun Context.getSmoothScroller(): SmoothScroller = object : LinearSmoothScroller(this) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
    override fun calculateDyToMakeVisible(view: View, snapPreference: Int): Int {

        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            140f + 22f,
            resources.displayMetrics
        )
        return super.calculateDyToMakeVisible(view, snapPreference) - px.toInt()
    }

}

interface RecyclerViewListenerController {
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
    fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
}

class ExperienceFeedRecyclerViewListener(
    val controller: RecyclerViewListenerController
): RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        controller.onScrollStateChanged(recyclerView, newState)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        controller.onScrolled(recyclerView, dx, dy)
    }
}
