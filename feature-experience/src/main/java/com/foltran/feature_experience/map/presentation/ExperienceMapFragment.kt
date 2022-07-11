package com.foltran.feature_experience.map.presentation

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.core.animation.addPauseListener
import androidx.core.content.ContextCompat
import com.foltran.core_experience.shared.data.model.getSampleExperienceHighlightSet
import com.foltran.core_map.core.models.icon.MapIcon
import com.foltran.core_map.core.models.toDoubleFormat
import com.foltran.core_map.core.presentation.model.ExperienceHighlightEventStatus
import com.foltran.core_map.core.presentation.model.PlayStatuses
import com.foltran.core_map.core.utils.extensions.*
import com.foltran.core_map.core.utils.extensions.photos.ExperiencePhotoManager
import com.foltran.core_map.experience.player.ExperiencePlayerStaticData
import com.foltran.core_map.experience.player.ExperiencePlayerStaticDataEventStatuses
import com.foltran.core_map.experience.player.ExperiencePlayerStatuses
import com.foltran.core_map.experience.webview.ExperiencePlayerWebViewInterface
import com.foltran.core_map.webview.MapPlayerWebViewInterface
import com.foltran.core_map.webview.loadWebView
import com.foltran.core_map.webview.toPhotoMapByIndexInStream
import com.foltran.core_map.webview.toPhotoMapByLatLng
import com.foltran.core_ui.animations.zoomImageFromThumb
import com.foltran.core_utils.bases.BaseFragment
import com.foltran.core_utils.binding.delegate.dataBinding
import com.foltran.core_utils.extensions.DURATION_MEDIUM_ANIMATION
import com.foltran.core_utils.extensions.buildVideoWrapper
import com.foltran.core_utils.extensions.fadeAnimation
import com.foltran.core_utils.extensions.toBase64
import com.foltran.feature_experience.R
import com.foltran.feature_experience.core.domain.model.toMapExperienceHighlightSet
import com.foltran.feature_experience.core.domain.model.toPathHighlightSet
import com.foltran.feature_experience.core.presentation.ExperienceViewModel
import com.foltran.feature_experience.databinding.FragmentExperienceMapBinding
import com.foltran.feature_experience.map.di.ExperienceMapModule
import com.foltran.feature_experience.map.presentation.adapters.retriveVideoFrameFromVideo
import com.foltran.feature_experience.map.presentation.adapters.tempVideoUrl
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class ExperienceMapFragment :
    BaseFragment(R.layout.fragment_experience_map, ExperienceMapModule.instance) {

    private val binding: FragmentExperienceMapBinding by dataBinding()

    private val mapView get() = binding.root.findViewById<View>(R.id.mapView)

    private val viewModel: ExperienceMapViewModel by viewModel()
    private val sharedViewModel: ExperienceViewModel by sharedViewModel()

    private val mapCoreUIModelObservable get() = sharedViewModel.mapObservable
    private val mapCoreMainViewObservable get() = sharedViewModel.mapCoreMainViewObservable
    private val mapCoreDisplayConfigObservable get() = sharedViewModel.mapUIModel.mapDisplayConfigObservable

    private var currentAnimator: Animator? = null
    private var shortAnimationDuration: Int = 0


    private val unBlurAnimation = ValueAnimator()
    private val unBlurAnimationDuration = 500

    private val blurAnimation = ValueAnimator()
    private val blurAnimationDuration = 500
    private var blurAnimationPauseRequestPending = false

    private val videoAnimationDuration = DURATION_MEDIUM_ANIMATION.toLong()
    private lateinit var experiencePhotoManager: ExperiencePhotoManager

    private var videoThumbnail: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)
        lifecycle.addObserver(sharedViewModel)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.sharedViewModel = sharedViewModel

        setupObservers()
        initViewModel()
        mapView.initMapStyle(requireContext())
        setupListeners()
        setupZoomableImage()
        setupBlurAnimation()
        setupUnBlurAnimation()
        getVideoThumbnails()
    }

    private fun getVideoThumbnails() {
        videoThumbnail = retriveVideoFrameFromVideo(tempVideoUrl)
            ?.copy(Bitmap.Config.ARGB_8888, true)
            ?.buildVideoWrapper(requireContext())
    }

    private fun initViewModel() {
        mapCoreUIModelObservable.observe(viewLifecycleOwner) {
            viewModel.mapObserver(it)
            experiencePhotoManager = ExperiencePhotoManager(
                mapView,
                it.photos,
                { photo ->
                    sharedViewModel.getPhotoUrl(photo.id) { url ->
                        openImageInZoom(photoUrl = url)
                    }
                },
                requireContext()
            )
        }
        mapCoreMainViewObservable.observe(viewLifecycleOwner) {
            viewModel.updateMapCoreMainView(it)
        }
        mapCoreDisplayConfigObservable.observe(viewLifecycleOwner) {
            viewModel.mapDisplayConfigObserver(it)
        }

    }


    private fun loadExperienceAnimationWebView() {

        setupPlayerObservers()

        val experiencePlayerStaticData = sharedViewModel.experiencePlayerStaticData.apply {
            videosByIndexInStream = HashMap<Int, MapIcon>().apply {
                put(
                    300, MapIcon(
                        id = "video_1",
                        lat = 0.0,
                        lon = 0.0,
                        indexInStream = 300,
                        base64 = videoThumbnail?.toBase64() ?: ""
                    )
                )
            }
        }
        val jsInterface = ExperiencePlayerWebViewInterface(
            mapCorePlayer = sharedViewModel.experiencePlayer,
            mapCorePlayerStaticData = experiencePlayerStaticData
        )

        requireContext().loadWebView(
            binding.webView,
            jsInterface
        )
    }

    override fun onPause() {
        super.onPause()
        binding.experienceVideoView.mp.stop()
        unBlurAnimation.cancel()
        blurAnimation.cancel()
    }


    private fun setupListeners() {
        mapView.addOnMapLoadedListener {
            mapCoreUIModelObservable.value?.let {
                mapView.updateMapCamera(mapCoreDisplayConfigObservable.value)
            }
        }
    }

    private fun setupObservers() {

        sharedViewModel.loadWebViewEvent.observe(viewLifecycleOwner) {
            loadExperienceAnimationWebView()
        }

        sharedViewModel.videoEvent.observe(viewLifecycleOwner) {
            playVideo {
                it.callback()
            }
        }

        sharedViewModel.zoomedImageEvent.observe(viewLifecycleOwner) {
            openImageInZoom(
                photoUrl = it.imageUrl,
                onZoomedImageCloseCallback = it.onZoomedImageCloseCallback,
                closeOnTimer = it.closeOnTimer,
                shouldCloseOnClick = it.shouldCloseOnClick
            )
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                ExperienceMapState.UpdateMapCamera -> {
                    mapView.updateMapCamera(
                        mapCoreDisplayConfigObservable.value
                    )
                }
                ExperienceMapState.UpdateMapPolyline -> mapView.updateMapPolyline(
                    mapCoreUIModelObservable.value
                )
                ExperienceMapState.UpdateMapFocusCoordinates -> {
//                    mapView.updateMapFocusCoordinatesPolyline(
//                    mapCoreUIModelObservable.value
//                )
                }
                ExperienceMapState.UpdateMapStartBeatle -> {
                    mapCoreUIModelObservable.value?.let {
                        mapView.addStartBeatleToMap(
                            it.startLatitude,
                            it.startLongitude
                        )
                    }
                }
                ExperienceMapState.InitTransitionToPlayMap -> mapView.moveCameraToStartOfPath(
                    mapCoreUIModelObservable = mapCoreUIModelObservable.value,
                    mapCoreDisplayConfig = mapCoreDisplayConfigObservable.value,
                    callbackToPlayMap = {
                        viewModel.callBackToPlayMap()
                    }
                )
            }
        }

        sharedViewModel.experienceHighlightsAddToMapEvent.observe(viewLifecycleOwner) {
            mapView.addHighlightsToMap(
                experienceHighlightSet = it.toMapExperienceHighlightSet(),
                coordinateStream = sharedViewModel.experiencePlayer.streams.completeCoordinatesStream.toDoubleFormat()
            )
        }

    }

    private fun setupPlayerObservers() {

        sharedViewModel.experiencePlayer.status.observe(viewLifecycleOwner) {

            binding.componentLiveDataBundle.curDistanceLotti.let { lotti ->
                if (it == ExperiencePlayerStatuses.PLAYING) lotti.resumeAnimation()
                else lotti.pauseAnimation()
            }
        }

        val videoIndex = 300 //matches .js for now
        sharedViewModel.experiencePlayer.streams.completeCoordinatesStream[videoIndex].let {
            videoThumbnail?.let { thumbnailBitmap ->
                mapView.addVideoBeatleToMap(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    thumbnailBitmap = thumbnailBitmap
                )
            }
        }

        sharedViewModel.experiencePlayerStaticData.highlightEvent.observe(viewLifecycleOwner) {
            when(it) {
                ExperiencePlayerStaticDataEventStatuses.PLAYING -> playAnimation()
                ExperiencePlayerStaticDataEventStatuses.FINISHED -> unBlurAnimation.start()
            }
        }

        sharedViewModel.experiencePlayer.streams.let { streams ->
            with(binding.componentLiveDataBundle) {
                chartLiveData.setDataPoints(streams.elevationStream.values)
            }
        }

        sharedViewModel.experiencePlayer.curPhase.observe(viewLifecycleOwner) {
            binding.componentLiveDataBundle.chartLiveData.setTime(it)
        }

        sharedViewModel.experiencePlayer.streams.speedStream.currentRatio.observe(viewLifecycleOwner) {
            binding.componentLiveDataBundle.curPaceBar.setTime(it)
        }
    }

    private fun setupZoomableImage() {
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    private fun playVideo(onVideoCompletedCallback: () -> Unit) {

        binding.experienceVideoView.mp.setOnCompletionListener {
            binding.experienceVideoViewBlur.fadeAnimation(
                fadeIn = true,
                duration = videoAnimationDuration,
                onAnimationEnd = {
                    onVideoCompletedCallback()
                    binding.experienceVideoView.visibility = View.INVISIBLE
                    binding.experienceVideoViewBackground.visibility = View.INVISIBLE
                    binding.experienceVideoViewBlur.fadeAnimation(
                        fadeIn = false,
                        duration = videoAnimationDuration
                    )
                })
        }

        binding.experienceVideoViewBlur.fadeAnimation(
            fadeIn = true,
            duration = videoAnimationDuration,
            onAnimationEnd = {
                binding.experienceVideoView.visibility = View.VISIBLE
                binding.experienceVideoView.mp.start()
                binding.experienceVideoViewBackground.visibility = View.VISIBLE
                binding.experienceVideoViewBlur.fadeAnimation(
                    fadeIn = false,
                    duration = videoAnimationDuration
                )
            })
    }

    private fun openImageInZoom(
        photoUrl: String,
        onZoomedImageCloseCallback: (() -> Unit)? = {},
        closeOnTimer: Int? = null,
        shouldCloseOnClick: Boolean = true
    ) {
        with(binding.zoomableImageContainer) {
            zoomImageFromThumb(
                container = container,
                thumbView = thumbButton1,
                currentAnimator = currentAnimator,
                expandedImageView = expandedImage,
                imageUrl = photoUrl,
                shortAnimationDuration = shortAnimationDuration.toLong(),
                setCurrentAnimator = {
                    currentAnimator = it
                },
                onZoomedImageCloseCallback = onZoomedImageCloseCallback,
                closeOnTimer = closeOnTimer,
                shouldCloseOnClick = shouldCloseOnClick
            )
        }
    }

    private fun playAnimation() {
        if (!blurAnimation.isRunning) {
            blurAnimation.start()
        }
    }

    private fun setupBlurAnimation() {

        val fromTotalTransparent = ContextCompat.getColor(requireContext(), R.color.translucent)
        val to = ContextCompat.getColor(requireContext(), R.color.blur_red_transparent)

        with(blurAnimation) {

            addPauseListener {
                blurAnimationPauseRequestPending = false
            }
            setIntValues(fromTotalTransparent, to)
            setEvaluator(ArgbEvaluator())
            addUpdateListener { valueAnimator ->
                binding.webViewBlur.setBackgroundColor(
                    valueAnimator.animatedValue as Int
                )
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    //start()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            duration = blurAnimationDuration.toLong()
        }
    }

    private fun setupUnBlurAnimation() {

        val from = ContextCompat.getColor(requireContext(), R.color.blur_red_transparent)
        val to = ContextCompat.getColor(requireContext(), R.color.translucent)

        with(unBlurAnimation) {

            setIntValues(from, to)
            setEvaluator(ArgbEvaluator())
            addUpdateListener { valueAnimator ->
                binding.webViewBlur.setBackgroundColor(
                    valueAnimator.animatedValue as Int
                )
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    //start()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            duration = unBlurAnimationDuration.toLong()
        }
    }

    companion object {
        fun newInstance() = ExperienceMapFragment()
    }
}