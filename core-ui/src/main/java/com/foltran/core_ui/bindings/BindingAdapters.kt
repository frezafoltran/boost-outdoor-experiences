package com.foltran.core_ui.bindings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.foltran.core_ui.R
import com.foltran.core_ui.components.GraphComponentParams
import com.foltran.core_ui.databinding.ComponentGraphItemBinding

@BindingAdapter("layoutMargin")
fun setLayoutMarginBottom(view: View, dimen: Float){
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(dimen.toInt())
    view.layoutParams = layoutParams
}

@BindingAdapter("goneUnless")
fun View.setGoneUnless(goneUnless: Boolean?){
    visibility = if (goneUnless == true) View.VISIBLE else View.GONE
}

@BindingAdapter("invisibleUnless")
fun View.setInvisibleUnless(invisibleUnless: Boolean){
    visibility = if (invisibleUnless) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("heightMatchParent")
fun View.setToolbarIconHeight(heightMatchParent: Boolean){
    updateLayoutParams {
        height = if (heightMatchParent) Toolbar.LayoutParams.MATCH_PARENT else Toolbar.LayoutParams.WRAP_CONTENT
    }
}

@BindingAdapter("layoutHeight")
fun View.setLayoutHeight(dimen: Float){
    val layoutParamsNew = layoutParams as ViewGroup.LayoutParams
    layoutParamsNew.height = dimen.toInt()
    this.layoutParams = layoutParams
}

@BindingAdapter("android:layout_weight")
fun setLayoutWeight(view: View, weight: Float) {
    val layoutParams = view.layoutParams as? LinearLayout.LayoutParams
    layoutParams?.let {
        it.weight = weight
        view.layoutParams = it
    }
}

@BindingAdapter("graphParams")
fun LinearLayout.buildGraph(graphParams: GraphComponentParams){

    val max: Double = graphParams.vals.maxOrNull() ?: 0.0
    val numDataPoints = graphParams.vals.size.toDouble()

    val baseWidth = resources.displayMetrics.widthPixels.toDouble()
    val baseHeight = layoutParams.height.toDouble()

    graphParams.vals.forEachIndexed{ i, item ->
            val factor = 1.0 - (max - item) / max

            ComponentGraphItemBinding.inflate(
                LayoutInflater.from(context),
                this,
                true
            ).also { binding ->
                binding.root.layoutParams = binding.root.layoutParams.also { params ->

                    params.height = (baseHeight * factor).toInt()
                    params.width = (baseWidth / numDataPoints).toInt()
                }

                if (i % 3 == 0){
                    binding.xAxisIndicator.layoutParams = binding.xAxisIndicator.layoutParams.also { params ->
                        params.height = (params.height.toDouble() * 1.5 ).toInt()
                        params.width = (params.width.toDouble() * 1.5 ).toInt()
                    }

                    binding.label.visibility = View.VISIBLE
                    binding.label.text = item.toString()
                }
            }

    }
}