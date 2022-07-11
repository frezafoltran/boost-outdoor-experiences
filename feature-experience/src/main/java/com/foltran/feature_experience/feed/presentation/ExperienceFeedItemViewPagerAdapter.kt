package com.foltran.feature_experience.feed.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.foltran.core_ui.bindings.loadImageUrl
import com.foltran.feature_experience.R
import com.foltran.feature_experience.core.presentation.adapter.setPreviewMap
import com.foltran.feature_experience.feed.presentation.adapter.ItemClickListener
import java.util.*

internal class ExperienceFeedItemViewPagerAdapter(
    var context: Context,
    var images: List<String>,
    var item: ItemUI,
    var clickListener: ItemClickListener,
    var previewMap: String,
    var rvPosition: Int
) :
    PagerAdapter() {
    // Layout Inflater
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.item_experience_feed_view_pager_item, container, false)

        val imageView = itemView.findViewById<View>(R.id.image) as ImageView

        if (position == 0){
            imageView.setPreviewMap(previewMap)
        }
        else{
            imageView.loadImageUrl(images.get(position))
        }

        itemView.setOnClickListener {
            clickListener.onClickItem(item, rvPosition)
        }

        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    init {
        images = images
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}
