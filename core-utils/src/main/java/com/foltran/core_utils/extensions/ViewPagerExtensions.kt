package com.foltran.core_utils.extensions

import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

open class PagerAdapterBase(open var context: Context, open var data: List<String>): PagerAdapter() {
    override fun getCount(): Int = data.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = false
}

fun ViewPager.setupTabDotsMultiElementPerPageViewPager(data: List<String>, tabDots: TabLayout){

    val elementsPerScreen = 3
    val fakeList = data.subList(0, data.size - elementsPerScreen + 1)
    val fakeAdapter = PagerAdapterBase(context, fakeList)
    val fakeViewPager = ViewPager(context).apply{ adapter = fakeAdapter }

    addOnPageChangeListener(
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                fakeViewPager.currentItem = position.coerceAtMost(fakeList.size - 1)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    fakeViewPager.addOnPageChangeListener(
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}

            override fun onPageSelected(position: Int) {
                currentItem = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


    tabDots.setupWithViewPager(fakeViewPager, true)
}