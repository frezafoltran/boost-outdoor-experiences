package com.foltran.feature_profile.map.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_map.core.utils.extensions.initMapStyle
import com.foltran.core_map.webview.ExperienceHeatMapWebViewInterface
import com.foltran.core_map.webview.ExperienceItemHeatMapExcerpt
import com.foltran.core_map.webview.loadWebView
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.getHeight
import com.foltran.core_utils.extensions.getWidth
import com.foltran.feature_profile.R
import com.foltran.feature_profile.core.presentation.ProfileViewModel
import com.foltran.feature_profile.databinding.FragmentProfileMapBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ProfileMapFragment : BaseFragment(R.layout.fragment_profile_map) {

    private val binding by dataBinding<FragmentProfileMapBinding>()
    private val sharedViewModel by lazy {
        requireParentFragment().getViewModel<ProfileViewModel>()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        sharedViewModel.feedItems.observe(viewLifecycleOwner) {
            setupMap(it)
        }
    }

    private fun setupMap(experiences: List<ExperienceItem>) {

        requireContext().loadWebView(
            binding.webView,
            ExperienceHeatMapWebViewInterface(
                experiences = experiences.map {
                    ExperienceItemHeatMapExcerpt(
                        startCoordinates = listOf(it.startLatitude, it.startLongitude),
                        polyline = it.map.summaryPolyline ?: it.map.polyline ?: ""
                    )
                }
            )
        ) {
            Handler().postDelayed({
                binding.webView.visibility = View.VISIBLE
            }, 50)
        }
    }

    companion object {
        fun newInstance() = ProfileMapFragment()
    }
}