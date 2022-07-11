package com.foltran.feature_experience.settings.photos.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import com.foltran.core_map.webview.loadWebView
import com.foltran.core_utils.bases.BaseActivity
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.getWidth
import com.foltran.core_utils.extensions.toBase64
import com.foltran.core_utils.extensions.toPx
import com.foltran.feature_experience.R
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.feature_experience.core.presentation.ExperienceActivity
import com.foltran.feature_experience.databinding.ActivityExperienceSettingsPhotosBinding
import com.foltran.feature_experience.settings.photos.di.ExperienceSettingsPhotosModules
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class ExperienceSettingsPhotosActivity :
    BaseActivity(
        R.layout.activity_experience_settings_photos,
        *ExperienceSettingsPhotosModules
    ) {

    private val binding: ActivityExperienceSettingsPhotosBinding by dataBinding()
    private val viewModel: ExperienceSettingsPhotosViewModel by viewModel {
        parametersOf(intent.getParcelableExtra<Experience>(EXPERIENCE_PARAM))
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val intent: Intent? = result.data
                intent?.data?.toBase64(this.contentResolver)?.let {
                    viewModel.addPhotoCommit(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupObservers()
        loadExperienceWebView()
    }

    private fun setupObservers() {
        viewModel.state.observe(this) {
            when (it) {
                ExperienceSettingsPhotoState.AddPhoto -> openGalleryToPickImage()
                ExperienceSettingsPhotoState.FinishActivity -> setSuccessAndFinish()
            }
        }

        viewModel.valsIndexToHighlight.observe(this) {
            binding.cameraIconsRow.removeAllViews()
            it.sortedWith { p0, p1 -> ((p0?.first ?: 0.0) - (p1?.first ?: 0.0)).toInt() }
                .forEach { elem ->
                    addPhotoIconAtRow(elem.first, elem.second[0], elem.second[1])
                }
        }
    }

    private fun addPhotoIconAtRow(ratio: Double, lat: Double, lon: Double) {
        val iconSize = 12f.toPx
        val proportionalPadding =
            (windowManager.getWidth().toDouble() * ratio - iconSize.toDouble()).toInt()
        ImageView(this).also {

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(proportionalPadding, 0, 0, 0)
            it.layoutParams = lp

            it.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_camera_white))
            it.setOnClickListener {
                viewModel.selectPhoto(lat, lon)
            }
            binding.cameraIconsRow.addView(it)
        }
    }


    private fun loadExperienceWebView() {

        val jsInterface = ExperienceSettingsPhotoWebViewInterface(
            startLngLat = viewModel.experience.value!!.startLatLng,
            polyline = viewModel.experience.value!!.map.polyline,
            currentLat = viewModel.currentLat,
            currentLng = viewModel.currentLng,
            currentPhotos = viewModel.currentPhotos,
            hasPendingPhotosChange = viewModel.hasPendingPhotosChange
        )

        loadWebView(binding.photosWebView, jsInterface)
    }

    private fun openGalleryToPickImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        resultLauncher.launch(intent)
    }

    private fun setSuccessAndFinish() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

        Intent().also {
            it.putExtra(ExperienceActivity.EXPERIENCE_PARAM, viewModel.experience.value)
            setResult(Activity.RESULT_OK, it)
        }
        onBackPressed()
    }

    companion object {
        const val EXPERIENCE_PARAM = "experienceParam"
    }
}