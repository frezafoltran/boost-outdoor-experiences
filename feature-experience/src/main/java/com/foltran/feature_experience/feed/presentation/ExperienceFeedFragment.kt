package com.foltran.feature_experience.feed.presentation


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.animateY
import com.foltran.core_utils.extensions.getHeight
import com.foltran.core_utils.extensions.replace
import com.foltran.feature_experience.R
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.core.presentation.ExperienceActivity
import com.foltran.feature_experience.core.presentation.ExperienceActivity.Companion.EXPERIENCE_PARAM
import com.foltran.feature_experience.databinding.FragmentExperienceFeedBinding
import com.foltran.feature_experience.feed.di.ExperienceFeedModule
import com.foltran.feature_experience.feed.presentation.listener.ExperienceFeedRecyclerViewListener
import com.foltran.feature_experience.feed.presentation.listener.RecyclerViewListenerController
import com.foltran.feature_experience.feed.presentation.listener.getSmoothScroller
import com.foltran.feature_experience.menu.presentation.ExperienceMenuFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.foltran.core_ui.bindings.setGoneUnless
import com.foltran.feature_experience.feed.presentation.adapter.ExperienceFeedAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest


interface AppBarHeightInterface {
    fun getAppBarHeight(): Int
}

class ExperienceFeedFragment :
    BaseFragment(
        R.layout.fragment_experience_feed,
        listOf(ExperienceFeedModule.instance)
    ) {

    private val viewModel: ExperienceFeedViewModel by viewModel()
    private val binding: FragmentExperienceFeedBinding by dataBinding()
    lateinit var smoothScroller: RecyclerView.SmoothScroller

    private var isBottomExperienceDetailHidden = true

    var itemClickHappened = false

    private var screenHeight: Int = 0
    private var experienceMenuFragment = ExperienceMenuFragment.newInstance()

    private lateinit var adapter: ExperienceFeedAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        screenHeight = requireActivity().windowManager.getHeight()


        setupFocusedExperienceFragment()

        smoothScroller = requireContext().getSmoothScroller()

        setupRecyclerViewListener()
        setupListeners()
        setupObservers()
        initAdapter()

        with(binding.tempMarker) {
            setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {

                    var x = p1?.x?.toDouble()
                    var y = p1?.y?.toDouble()

                    if (x == null || y == null) return false

                    var bitmap = experienceMenuFragment.clickedBitmap(
                        x / width.toDouble(),
                        y / height.toDouble()
                    )
                    if (bitmap != null){
                        Log.i("JVFF bm clicked", bitmap.toString())
                    }
                    return false
                }
            })
        }

    }

    private fun initAdapter() {

        adapter = ExperienceFeedAdapter(viewModel, requireContext())

        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collect {
                binding.srlLoader.setGoneUnless(it.mediator?.refresh is LoadState.Loading)
                binding.srl.isRefreshing = false
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.feed.collectLatest {
                //adapter.submitListInScope(it)
                adapter.submitData(it)
            }
        }

        binding.srl.setOnRefreshListener {
            adapter.refresh()
            binding.srl.isRefreshing = false
        }
    }

    private fun setupFocusedExperienceFragment() {
        with(binding.tempBottomData) {
            y = screenHeight.toFloat()
            childFragmentManager.replace(
                R.id.tempBottomData,
                experienceMenuFragment
            )
        }
    }

    fun hideLowerExperienceDetailsSheet() {

        with(binding.tempBottomData) {
            if (isBottomExperienceDetailHidden) return
            binding.tempMarker.visibility = View.INVISIBLE
            binding.baseMarker.visibility = View.INVISIBLE
            val appBarHeight = (requireActivity() as AppBarHeightInterface).getAppBarHeight()

            animateY(
                screenHeight.toFloat() - height.toFloat() - appBarHeight,
                screenHeight.toFloat()
            )
            isBottomExperienceDetailHidden = true
        }
    }

    fun showLowerExperienceDetailsSheet() {
        //binding.tempMarkerLoader.visibility = View.VISIBLE

        with(binding.tempBottomData) {
            if (!isBottomExperienceDetailHidden) return
//            binding.tempMarker.visibility = View.VISIBLE
//            binding.baseMarker.visibility = View.VISIBLE
            val appBarHeight = (requireActivity() as AppBarHeightInterface).getAppBarHeight()

            animateY(
                screenHeight.toFloat(),
                screenHeight.toFloat() - height.toFloat() - appBarHeight
            )
            isBottomExperienceDetailHidden = false
        }
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ExperienceFeedViewState.GoToCurFocusedExperience -> {
                    it.experience?.let { experience ->
                        goToCurFocusedExperience(experience)
                    }
                }
            }
        }
    }

    private fun setupRecyclerViewListener() {
        (requireActivity() as RecyclerViewListenerController).let {
            binding.rv.addOnScrollListener(ExperienceFeedRecyclerViewListener(it))
        }
    }

    private fun goToCurFocusedExperience(experience: Experience) {
        Intent(requireContext(), ExperienceActivity::class.java).also {
            it.putExtra(EXPERIENCE_PARAM, experience)

            startActivity(it)
        }
    }

    private fun setupListeners() {

        experienceMenuFragment.focusedExperienceLapBitmap.observe(viewLifecycleOwner) {
            it?.let {
                binding.tempMarker.visibility = View.VISIBLE
                binding.baseMarker.visibility = View.VISIBLE
                binding.tempMarker.setImageBitmap(it.bitmap)
            }
        }

        experienceMenuFragment.focusedExperienceBaseBitmap.observe(viewLifecycleOwner) {
            it?.let {
                binding.baseMarker.visibility = View.VISIBLE
                binding.baseMarker.setImageBitmap(it.bitmap)
            }
        }

        viewModel.itemClickEvent.observe(viewLifecycleOwner) { params ->

            if (isBottomExperienceDetailHidden) {
                itemClickHappened = true

                experienceMenuFragment.updateCurFocusedExperience(
                    params.experienceId,
                )

                smoothScroller.targetPosition = params.itemPosition
                with(binding.rv.layoutManager as LinearLayoutManager) {
                    startSmoothScroll(smoothScroller)
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ExperienceFeedFragment()
    }
}