package com.foltran.core_utils.extensions

import android.animation.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator


const val DURATION_SHORT_ANIMATION: Int = 50
const val DURATION_SHORT_100_ANIMATION: Int = 100
const val DURATION_MEDIUM_ANIMATION: Int = 200
const val DURATION_LONG_ANIMATION: Int = 400

fun View.fadeAnimation(
    fadeIn: Boolean = true,
    onAnimationStart: (() -> Unit) = {},
    onAnimationEnd: (() -> Unit) = {},
    duration: Long = DURATION_SHORT_ANIMATION.toLong()
) {

    val startAlpha = if (fadeIn) 0f else 1f
    val endAlpha = if (fadeIn) 1f else 0f

    alpha = startAlpha

    animate()
        .alpha(endAlpha)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                onAnimationStart()
                if (fadeIn) visibility = View.VISIBLE
            }
            override fun onAnimationEnd(animation: Animator){
                onAnimationEnd()
            }
        })
}


fun View.animateHeight(finalHeight: Int){

    val slideAnimator = ValueAnimator
            .ofInt(height, finalHeight)
        .setDuration(DURATION_MEDIUM_ANIMATION.toLong());

    slideAnimator.addUpdateListener {  }

    slideAnimator.addUpdateListener {
        val value = it.getAnimatedValue()
        layoutParams.height = value as Int
        requestLayout();
    }

    val animationSet = AnimatorSet()
    animationSet.interpolator = AccelerateDecelerateInterpolator();
    animationSet.play(slideAnimator);
    animationSet.start()
}

fun View.animateHeightByFactor(factor: Double = 1.2){
    animateHeight((height * factor).toInt())
}

fun View.animateY(initialY: Float, finalY: Float, duration: Long = DURATION_MEDIUM_ANIMATION.toLong()): AnimatorSet {

    val slideAnimator = ValueAnimator
        .ofFloat(initialY, finalY)
        .setDuration(duration);

    slideAnimator.addUpdateListener {  }

    slideAnimator.addUpdateListener {
        val value = it.getAnimatedValue()
        y = value as Float
        requestLayout();
    }

    return AnimatorSet().apply {
        interpolator = AccelerateDecelerateInterpolator();
        play(slideAnimator);
        start()
    }
}

fun View.fadeAnimationAnimatorSet(
    startAlpha: Float,
    endAlpha: Float,
    duration: Long = DURATION_SHORT_100_ANIMATION.toLong()
): AnimatorSet {

    val slideAnimator = ValueAnimator
        .ofFloat(startAlpha, endAlpha)
        .setDuration(duration);

    slideAnimator.addUpdateListener {  }

    slideAnimator.addUpdateListener {
        val value = it.getAnimatedValue()
        alpha = value as Float
        requestLayout();
    }

    return AnimatorSet().apply {
        interpolator = LinearInterpolator()
        play(slideAnimator);
        start()
    }
}

