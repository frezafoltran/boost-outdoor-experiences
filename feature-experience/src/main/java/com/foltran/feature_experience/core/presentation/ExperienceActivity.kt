package com.foltran.feature_experience.core.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.foltran.core_map.core.presentation.model.MapCoreMainView
import com.foltran.core_map.core.presentation.model.PlayStatuses
import com.foltran.core_utils.bases.BaseActivity
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.getHeight
import com.foltran.core_utils.extensions.replace
import com.foltran.feature_experience.R
import com.foltran.feature_experience.chart.presentation.ExperienceChartActivity
import com.foltran.feature_experience.core.di.ExperienceMainModules
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.databinding.ActivityExperienceBinding
import com.foltran.feature_experience.details.presentation.ExperienceDetailsFragment
import com.foltran.feature_experience.map.presentation.ExperienceMapFragment
import com.foltran.feature_experience.settings.photos.presentation.ExperienceSettingsPhotosActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


interface ExperienceActivityActions {
    fun finish()
}

class ExperienceActivity :
    BaseActivity(
        R.layout.activity_experience,
        *ExperienceMainModules
    ), ExperienceActivityActions {


    private val binding: ActivityExperienceBinding by dataBinding()
    private val viewModel: ExperienceViewModel by viewModel {
        parametersOf(
            intent.getParcelableExtra<Experience>(EXPERIENCE_PARAM)
        )
    }
    private val bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
        get() =
            from(binding.frameLayoutHolderExperienceMain)

    private val mapFragment = ExperienceMapFragment.newInstance()
    private val experienceMainFragment = ExperienceDetailsFragment.newInstance()

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<Experience>(EXPERIENCE_PARAM)?.let { experience ->
                    viewModel.updateExperience(experience)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("JVFF", "onCreate ExperienceActivity")
        binding.actions = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupStatusBar()
        setupFragments()
        setupBottomSheet()
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.mapUIModel.updateDisplayConfigOnBottomSheetChange(
            screenHeight = windowManager.getHeight().toDouble()
        )
    }

    private fun setupObservers() {
        viewModel.state.observe(this) {
            when (it) {
                is ExperienceMainState.ExpandChart -> goToChartActivity()
                is ExperienceMainState.OpenShareTray -> openShareTray()
                is ExperienceMainState.GoToSettingsActivity -> goToExperienceSettingsActivity(it.experience)
                is ExperienceMainState.ToggleSettingsDrawer -> toggleSettingsDrawer(it.close)
                ExperienceMainState.ExpandBottomSheet -> expandBottomSheet()
            }
        }

        viewModel.mapCoreMainViewObservable.observe(this) {
            when (it) {
                MapCoreMainView.MAP -> {
                    halfExpandBottomSheet()
                }
                MapCoreMainView.WEB_VIEW -> {
                    collapseBottomSheet()
                }
            }
        }
    }

    private fun setupStatusBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_primary)
    }

    private fun setupBottomSheet() {

        bottomSheetBehavior.apply {

            isFitToContents = false
            halfExpandedRatio = 0.5f
            expandedOffset =
                resources.getDimensionPixelSize(R.dimen.experience_main_bottom_sheet_y_offset)
            state = DEFAULT_BOTTOM_SHEET_STATE

            addBottomSheetCallback(object : BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    viewModel.mapUIModel.updateDisplayConfigOnBottomSheetChange(
                        newState,
                        windowManager.getHeight().toDouble()
                    )


                    if (viewModel.mapCoreMainViewObservable.value == MapCoreMainView.WEB_VIEW && (newState == STATE_EXPANDED || newState == STATE_HALF_EXPANDED )) {
                        viewModel.experiencePlayer.finish()
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    viewModel.screenUIModel.updateViewPagerOnBottomSheetChange(slideOffset)
                }
            })
        }
    }

    private fun toggleSettingsDrawer(close: Boolean) {
        with(binding.experienceDrawerLayout) {
            if (close) closeDrawer(GravityCompat.END)
            else openDrawer(GravityCompat.END)
        }
    }

    private fun expandBottomSheet() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }

    private fun halfExpandBottomSheet() {
        bottomSheetBehavior.state = STATE_HALF_EXPANDED
    }

    private fun collapseBottomSheet() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    private fun goToExperienceSettingsActivity(experience: Experience) {

        Intent(this, ExperienceSettingsPhotosActivity::class.java).also {
            it.putExtra(ExperienceSettingsPhotosActivity.EXPERIENCE_PARAM, experience)
            resultLauncher.launch(it)
        }
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

    }

    private fun setupFragments() {
        supportFragmentManager.replace(
            id = R.id.frameLayoutHolderExperienceMap,
            fragment = mapFragment
        )
        supportFragmentManager.replace(
            id = R.id.frameLayoutHolderExperienceMain,
            fragment = experienceMainFragment
        )
    }

    override fun onBackPressed() {
        collapseBottomSheet()
        finish()
    }

    private fun goToChartActivity() {
        startActivity(
            Intent(this, ExperienceChartActivity::class.java).putExtra(
                ExperienceChartActivity.EXPERIENCE_CHART_COMMANDS,
                viewModel.chartSpinnerUI.chartData.value
            )
        )
    }

    private fun openShareTray() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, "Export your experience to others!"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    companion object {
        const val EXPERIENCE_PARAM = "experienceParam"
        const val EXPERIENCE_STREAM_PARAMS = "experienceStreamParams"
        private const val DEFAULT_BOTTOM_SHEET_STATE = STATE_HALF_EXPANDED
    }

}