package com.foltran.feature_profile.feed.presentation

import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.DURATION_SHORT_ANIMATION
import com.foltran.core_utils.extensions.animateY
import com.foltran.core_utils.extensions.fadeAnimationAnimatorSet
import com.foltran.feature_profile.R
import com.foltran.feature_profile.core.presentation.ProfileViewModel
import com.foltran.feature_profile.databinding.FragmentProfileFeedBinding
import com.foltran.feature_profile.feed.di.ProfileFeedModule
import com.foltran.feature_profile.feed.presentation.adapter.ProfileFeedAdapter
import com.foltran.feature_profile.feed.presentation.adapter.ProfileFeedItemViewHolder
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFeedFragment :
    BaseFragment(R.layout.fragment_profile_feed, ProfileFeedModule.instance) {

    private lateinit var feedAdapter: ProfileFeedAdapter
    private val binding by dataBinding<FragmentProfileFeedBinding>()
    private val sharedViewModel by lazy {
        requireParentFragment().getViewModel<ProfileViewModel>()
    }
    private val viewModel by viewModel<ProfileFeedViewModel>()

    private var stickyHeaderAnimationShow: AnimatorSet? = null
    private var stickyHeaderAnimationHide: AnimatorSet? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        toggleStickyHeader()
        setupObservers()
        initAdapter()
        setupSpinner()
    }

    private fun setupSpinner() {

        val years = arrayOf("date", "distance", "location")
        val langAdapter = ArrayAdapter<CharSequence>(requireContext(), R.layout.spinner_text_profile_feed, years)
        langAdapter.setDropDownViewResource(R.layout.spinner_dropdown_profile_feed)

        with(binding.curFilterOptionsSpinner) {
            adapter = langAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    lifecycleScope.launchWhenCreated {

                        when(position){
                            0 -> sharedViewModel.feedByDate.collectLatest {
                                feedAdapter.submitDataAndUpdateLabelType(it, ProfileFeedItemViewHolder.Companion.LabelTypes.DATE_MONTH_DAY)
                            }
                            1 -> sharedViewModel.feedByDistance.collectLatest {
                                feedAdapter.submitDataAndUpdateLabelType(it, ProfileFeedItemViewHolder.Companion.LabelTypes.DISTANCE)
                            }
                            2 -> sharedViewModel.feedByLocation.collectLatest {
                                feedAdapter.submitDataAndUpdateLabelType(it, ProfileFeedItemViewHolder.Companion.LabelTypes.DATE_MONTH_DAY_YEAR)
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }

        binding.curFilterOptionsSpinnerWrapper.setOnClickListener {
            binding.curFilterOptionsSpinner.performClick()
        }

    }

    private fun setupObservers() {
        viewModel.toggleFeedZoomEvent.observe(viewLifecycleOwner) {

            binding.rvOverlayAction.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    if (feedAdapter.toggleColumns() == ProfileFeedAdapter.SPAN_ZOOM_IN) {
                        R.drawable.ic_zoom_out
                    } else R.drawable.ic_zoom_in
                )
            )

        }
    }

    private fun initAdapter() {

        feedAdapter = ProfileFeedAdapter(binding.rv, viewModel, requireContext())

        binding.rv.adapter = feedAdapter
        binding.rv.layoutManager =
            GridLayoutManager(requireContext(), ProfileFeedAdapter.TOTAL_SPAN).apply {
                spanSizeLookup = feedAdapter.scalableSpanSizeLookUp
            }

        binding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val headerText = viewModel.onFeedScrolledUpdateStickyHeader(recyclerView)
                toggleStickyHeader(headerText)
            }
        })

        lifecycleScope.launchWhenCreated {
            sharedViewModel.feedByDate.collectLatest {
                feedAdapter.submitDataAndUpdateLabelType(it, ProfileFeedItemViewHolder.Companion.LabelTypes.DATE_MONTH_DAY)
            }
        }

    }

    private fun toggleStickyHeader(label: String? = null) {
        val isAnimationShowRunning =
            stickyHeaderAnimationShow != null && stickyHeaderAnimationShow!!.isRunning
        val isAnimationHideRunning =
            stickyHeaderAnimationHide != null && stickyHeaderAnimationHide!!.isRunning

        with(binding.stickyLabel) {
            if (label != null) {
                text = label

                if (!isAnimationShowRunning) {
                    stickyHeaderAnimationHide?.cancel()
                    stickyHeaderAnimationShow = fadeAnimationAnimatorSet(alpha, 1f)
                }
            } else if (!isAnimationHideRunning) {
                stickyHeaderAnimationShow?.cancel()
                stickyHeaderAnimationHide = fadeAnimationAnimatorSet(alpha, 0f)
            }
        }
    }

    companion object {
        fun newInstance() = ProfileFeedFragment()
    }
}