package com.foltran.feature_experience.menu.presentation.adapters

import android.view.View
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.foltran.core_utils.viewpager.VerticalMarginItemDecoration
import com.foltran.feature_experience.R
import kotlin.math.abs

interface ExperienceMenuLapsSelector {
    fun setCurrentSelected(position: Int)
}

@BindingAdapter("menuLapsViewPagerData", "experienceMenuLapsSelector")
internal fun ViewPager2.setMenuLapsViewPagerData(viewPagerData: List<LapSelectorData>?, experienceMenuLapsSelector: ExperienceMenuLapsSelector?) {
    viewPagerData?.let {
        adapter = ExperienceMenuLapPagerAdapter(viewPagerData)
        offscreenPageLimit = 1

        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->

//            val pageTranslationPercentage = (page.height.toDouble()/1.4).toFloat()
            val pageTranslationPercentage = 500f
            page.translationY = -pageTranslationPercentage * position
            page.scaleY = 1 - (0.25f * abs(position))

            page.alpha = 0.5f + (1 - abs(position))
        }
        setPageTransformer(pageTransformer)

        val itemDecoration = VerticalMarginItemDecoration(
            context,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        addItemDecoration(itemDecoration)

        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                experienceMenuLapsSelector?.let {
                    it.setCurrentSelected(position)
                }
                //val selectedView = (get(0) as RecyclerView).layoutManager?.findViewByPosition(position)
                //selectedView?.setBackgroundResource(R.color.secondary)
            }
        })
    }
}
