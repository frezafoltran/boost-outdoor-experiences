package com.foltran.feature_experience.details.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.foltran.core_map.core.presentation.model.PlayStatuses
import com.foltran.core_ui.components.DragLinearLayout
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.feature_experience.R
import com.foltran.feature_experience.databinding.FragmentExperienceDetailsBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.foltran.core_ui.components.DraggableItemLayout
import com.foltran.feature_experience.core.presentation.ExperienceMainState
import com.foltran.feature_experience.core.presentation.ExperienceViewModel
import com.foltran.feature_experience.databinding.ComponentExperienceDetailsPlayingBinding
import com.foltran.feature_experience.databinding.ComponentExperienceDetailsPreviewBinding
import com.foltran.feature_experience.details.di.ExperienceDetailsModule


class ExperienceDetailsFragment : BaseFragment(
    R.layout.fragment_experience_details,
    ExperienceDetailsModule.instance
) {

    private val binding: FragmentExperienceDetailsBinding by dataBinding()

    private val sharedViewModel: ExperienceViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(sharedViewModel)

        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupDragLayout()
    }

    private fun setupDragLayout() {
        binding.experiencePreviewContainer.linearLayoutDraggable.let {
            it.setupDraggableViews()
            setupObservers(it)
        }
    }

    private fun DragLinearLayout.setupDraggableViews() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            setViewDraggable(child, child)
        }
    }

    private fun DragLinearLayout.toggleDraggableParam(draggable: Boolean) {
        setIsDraggable(draggable)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is DraggableItemLayout) {
                child.setIsDraggable(draggable)
            }
        }
    }

    private fun setupObservers(dragLinearLayout: DragLinearLayout) {
        sharedViewModel.screenUIModel.experienceDetailsItemsDraggable.observe(viewLifecycleOwner) {
            dragLinearLayout.toggleDraggableParam(it)
        }
    }

    companion object {
        fun newInstance() = ExperienceDetailsFragment()
    }
}
