package com.foltran.feature_profile.core.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.feature_profile.R
import com.foltran.feature_profile.core.di.ProfileModule
import com.foltran.feature_profile.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment(R.layout.fragment_profile, ProfileModule.instance) {

    private val viewModel: ProfileViewModel by viewModel()
    private val binding: FragmentProfileBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        binding.viewPager.adapter = ProfileTabsAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = AppCompatResources.getDrawable(requireContext(), when (position) {
                0 -> R.drawable.ic_grid
                1 -> R.drawable.ic_map
                else -> throw IllegalStateException()
            })
        }.attach()

        setupViewPager()

    }

    private fun setupViewPager() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.viewPager.isUserInputEnabled = binding.viewPager.currentItem == 0
            }
        })
        binding.viewPager.requestTransparentRegion(binding.viewPager)
    }


    companion object {
        fun newInstance() = ProfileFragment()
    }
}