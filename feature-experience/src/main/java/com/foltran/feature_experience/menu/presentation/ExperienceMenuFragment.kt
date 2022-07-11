package com.foltran.feature_experience.menu.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.foltran.core_map.images.utils.LapBitmap
import com.foltran.core_map.images.utils.saveStaticMapForLapsInInternalStorage
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.feature_experience.R
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.core.presentation.ExperienceActivity
import com.foltran.feature_experience.databinding.FragmentExperienceMenuBinding
import com.foltran.feature_experience.menu.di.ExperienceMenuModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExperienceMenuFragment : BaseFragment(
    R.layout.fragment_experience_menu,
    ExperienceMenuModule.instance
) {

    private val binding: FragmentExperienceMenuBinding by dataBinding()
    private val viewModel: ExperienceMenuViewModel by viewModel()

    val focusedExperienceLapBitmap = MutableLiveData<LapBitmap>()
    val focusedExperienceBaseBitmap = MutableLiveData<LapBitmap>()

    private var lastValue = 0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)

        setupObservers()
        setupTouchpad()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchpad() {

        binding.viewPagerLapSelectionTouchpad.getChildAt(0).setOnTouchListener { _, event ->
            handleTouch(event, 3f)
        }
        binding.viewPagerLapSelectionTouchpadFast.getChildAt(0).setOnTouchListener { _, event ->
            handleTouch(event, 8f)
        }
    }

    private fun handleTouch(event: MotionEvent, multiplier: Float): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = event.y
                binding.viewPagerLapSelection.beginFakeDrag()
            }

            MotionEvent.ACTION_MOVE -> {
                val value = event.y
                val delta = value - lastValue
                binding.viewPagerLapSelection.fakeDragBy(delta * multiplier)
                lastValue = value
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                binding.viewPagerLapSelection.endFakeDrag()
            }
        }
        return true
    }


    private fun setupObservers() {

        viewModel.saveImageByUrlToInternalStorage.observe(viewLifecycleOwner) {

            if (it.shouldSave) {
                saveStaticMapForLapsInInternalStorage(
                    experienceId = it.experienceId,
                    bitmaps = it.bitmaps,
                    context = requireContext()
                )
            }
            viewModel.updateLaps(it.bitmaps)

        }


        viewModel.focusedExperienceLapBitmap.observe(viewLifecycleOwner) {
            focusedExperienceLapBitmap.value = it
        }

        viewModel.focusedExperienceBaseBitmap.observe(viewLifecycleOwner) {
            focusedExperienceBaseBitmap.value = it
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is ExperienceMenuState.GoToExperience -> goToExperience(
                    it.experience,
                )
            }
        }

    }

    fun updateCurFocusedExperience(experienceId: String) {
        lastValue = 0f

        viewModel.updateCurFocusedExperience(experienceId)

//        lapBitmaps.sortedBy {
//            it.lapId
//        }.let {
//
//            if (it.isEmpty()) return@let
//
//            val sorted = listOf(it[it.size - 1]) + if (it.size > 1) it.subList(
//                0,
//                it.size - 1
//            ) else emptyList()
//
//            viewModel.updateCurFocusedExperience(experienceId, sorted)
//        }
    }

    fun clickedBitmap(xFactor: Double, yFactor: Double): LapBitmap? {

        viewModel.focusedExperienceLapBitmaps.value?.forEach {

            if (it.lapId.contains("base")) return@forEach

            val x = xFactor * it.bitmap.width.toDouble()
            val y = yFactor * it.bitmap.height.toDouble()

            val transparency = it.bitmap.getPixel(x.toInt(), y.toInt()) and -0x1000000 shr 24

            if (transparency == -1) return it
        }
        return null
    }

    private fun goToExperience(
        experience: Experience?,
    ) {
        experience?.let {
            Intent(requireContext(), ExperienceActivity::class.java).also {
                it.putExtra(ExperienceActivity.EXPERIENCE_PARAM, experience)
                startActivity(it)
            }
        }
    }

    companion object {

        fun newInstance() = ExperienceMenuFragment()
    }
}