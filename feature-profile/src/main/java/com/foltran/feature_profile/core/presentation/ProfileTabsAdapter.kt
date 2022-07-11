package com.foltran.feature_profile.core.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.foltran.feature_profile.feed.presentation.ProfileFeedFragment
import com.foltran.feature_profile.map.presentation.ProfileMapFragment

class ProfileTabsAdapter(fragmentHost: Fragment) : FragmentStateAdapter(fragmentHost) {

    override fun getItemCount(): Int = NUM_ITEMS

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> ProfileFeedFragment()
            1 -> ProfileMapFragment.newInstance()
            else -> throw IllegalStateException()
        }

    companion object {
        private const val NUM_ITEMS = 2
    }
}
