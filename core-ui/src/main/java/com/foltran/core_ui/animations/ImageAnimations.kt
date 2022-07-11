package com.foltran.core_ui.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import com.foltran.core_ui.R
import com.foltran.core_ui.bindings.loadImageUrl

/**
 * returns method to close the zoomed image
 */
fun zoomImageFromThumb(
    container: View,
    thumbView: View,
    currentAnimator: Animator?,
    expandedImageView: ImageView,
    imageUrl: String,
    shortAnimationDuration: Long,
    setCurrentAnimator: (animator: Animator?) -> Unit,
    onZoomedImageCloseCallback: (() -> Unit)? = {},
    closeOnTimer: Int? = null,
    shouldCloseOnClick: Boolean = true
) {

    currentAnimator?.cancel()
    val progressBar = container.findViewById<ProgressBar>(R.id.progressBar)

    val startBoundsInt = Rect()
    val finalBoundsInt = Rect()
    val globalOffset = Point()

    thumbView.getGlobalVisibleRect(startBoundsInt)
    container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
    startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
    finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

    val startBounds = RectF(startBoundsInt)
    val finalBounds = RectF(finalBoundsInt)

    val startScale: Float
    if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
        // Extend start bounds horizontally
        startScale = startBounds.height() / finalBounds.height()
        val startWidth: Float = startScale * finalBounds.width()
        val deltaWidth: Float = (startWidth - startBounds.width()) / 2
        startBounds.left -= deltaWidth.toInt()
        startBounds.right += deltaWidth.toInt()
    } else {
        // Extend start bounds vertically
        startScale = startBounds.width() / finalBounds.width()
        val startHeight: Float = startScale * finalBounds.height()
        val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
        startBounds.top -= deltaHeight.toInt()
        startBounds.bottom += deltaHeight.toInt()
    }

    thumbView.alpha = 0f

    // Set the pivot point for SCALE_X and SCALE_Y transformations
    // to the top-left corner of the zoomed-in view (the default
    // is the center of the view).
    expandedImageView.pivotX = 0f
    expandedImageView.pivotY = 0f


    fun resetProgressBar() {
        progressBar.visibility = View.GONE
        progressBar.progress = 0
    }

    fun closeZoomedImage() {
        currentAnimator?.cancel()

        resetProgressBar()

        setCurrentAnimator(AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
            }
            duration = shortAnimationDuration
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    setCurrentAnimator(null)
                    if (onZoomedImageCloseCallback != null) {
                        onZoomedImageCloseCallback()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    thumbView.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    setCurrentAnimator(null)
                    if (onZoomedImageCloseCallback != null) {
                        onZoomedImageCloseCallback()
                    }
                }
            })
            start()
        })
    }

    expandedImageView.setOnClickListener {
        if (shouldCloseOnClick) {
            closeZoomedImage()
        }
    }

    expandedImageView.loadImageUrl(imageUrl, onLoadedSuccessCallback = {

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        expandedImageView.visibility = View.VISIBLE

        setCurrentAnimator(AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expandedImageView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)

                    closeOnTimer?.let{

                        with (progressBar) {

                            Handler().postDelayed({
                                resetProgressBar()
                                closeZoomedImage()
                            }, closeOnTimer.toLong())

                            visibility = android.view.View.VISIBLE

                            val animation = android.animation.ObjectAnimator.ofInt(this, "progress", 0, 100)
                            animation.duration = closeOnTimer.toLong()
                            animation.interpolator = DecelerateInterpolator()
                            animation.addListener(object : Animator.AnimatorListener {
                                override fun onAnimationStart(animator: Animator) {}
                                override fun onAnimationEnd(animator: Animator) {
                                    //do something when the countdown is complete
                                }

                                override fun onAnimationCancel(animator: Animator) {}
                                override fun onAnimationRepeat(animator: Animator) {}
                            })
                            animation.start()
                        }
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    //currentAnimator = null
                    setCurrentAnimator(null)
                }

                override fun onAnimationCancel(animation: Animator) {
                    //currentAnimator = null
                    setCurrentAnimator(null)
                }
            })
            start()
        })
    })
}