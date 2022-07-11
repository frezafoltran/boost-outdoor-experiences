package com.foltran.feature_experience.core.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.foltran.core_ui.bindings.loadImageUrl
import com.foltran.feature_experience.databinding.ItemExperienceDataFieldBinding
import com.foltran.feature_experience.databinding.ItemExperiencePictureBinding
import java.util.*

open class ExperiencePicturesPagerAdapterBase(
    open var context: Context,
    open var data: List<String>
) : PagerAdapter() {
    override fun getCount(): Int = data.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = false
}

internal class ExperiencePicturesPagerAdapter(
    override var context: Context,
    override var data: List<String>
) :
    ExperiencePicturesPagerAdapterBase(context, data) {
    var mLayoutInflater: LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding = ItemExperiencePictureBinding.inflate(mLayoutInflater, container, false)

        binding.imageView.loadImageUrl(data[position])

        Objects.requireNonNull(container).addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }


    init {
        data = data
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}
