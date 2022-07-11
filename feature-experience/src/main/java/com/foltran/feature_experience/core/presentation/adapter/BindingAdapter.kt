package com.foltran.feature_experience.core.presentation.adapter

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.foltran.core_ui.bindings.loadImageUrl
import com.foltran.feature_experience.R
import com.foltran.feature_experience.core.presentation.chart.ExperienceChartCommands
import com.google.android.material.tabs.TabLayout


@BindingAdapter("isEnabled")
internal fun ViewPager.setIsEnabled(isEnabled: Boolean) {

    setOnTouchListener(if (isEnabled) null else { view, motionEvent -> true })

}

@BindingAdapter("experienceViewPagerData", "setupTabDots")
internal fun ViewPager.setExperienceDataFieldViewPager(
    viewPagerData: List<Pair<Int, String>>?,
    setupTabDots: Boolean? = false
) {

    viewPagerData?.let {
        adapter = ExperienceDataFieldPagerAdapter(context, it)

        if (setupTabDots == true)
            this.setupTabDotsMultiElement(it, this.rootView.findViewById(R.id.tabDots))
    }
}

@BindingAdapter("experiencePictureViewPagerData")
internal fun ViewPager.setExperiencePicturesViewPager(
    viewPagerData: List<String>?
) {

    viewPagerData?.let {
        adapter = ExperiencePicturesPagerAdapter(context, it)

        this.setupTabDots(it, this.rootView.findViewById(R.id.tabDotsPictures))
    }
}


fun ViewPager.setupTabDots(data: List<String>, tabDots: TabLayout) {
    tabDots.setupWithViewPager(this, true)
}

fun ViewPager.setupTabDotsMultiElement(data: List<Pair<Int, String>>, tabDots: TabLayout) {

    val elementsPerScreen = 3
    if (data.size - elementsPerScreen + 1 < 0) return

    val fakeList = data.subList(0, data.size - elementsPerScreen + 1)
    val fakeAdapter = ExperienceDataFieldPagerAdapterBase(context, fakeList)
    val fakeViewPager = ViewPager(context).apply { adapter = fakeAdapter }

    addOnPageChangeListener(
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                fakeViewPager.currentItem = position.coerceAtMost(fakeList.size - 1)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    fakeViewPager.addOnPageChangeListener(
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentItem = position
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })


    tabDots.setupWithViewPager(fakeViewPager, true)
}

@BindingAdapter("previewMap")
fun ImageView.setPreviewMap(previewMap: String?) {

    previewMap?.let{
        this.loadImageUrl(it)
    }
}

@BindingAdapter("experienceChartCommands")
fun ConstraintLayout.setupExperienceChartSpinner(experienceChartCommands: ExperienceChartCommands?) {

    experienceChartCommands?.let {
        findViewById<Spinner>(R.id.spinnerSelectGraph).let { spinner ->

            spinner.adapter = ArrayAdapter<CharSequence>(
                context,
                R.layout.spinner_text_small,
                it.spinnerCategories
            ).apply {
                setDropDownViewResource(R.layout.spinner_dropdown_small)
            }

            spinner.onItemSelectedListener =  object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                experienceChartCommands.onSelectCategory(position)
            }

        }

            findViewById<ImageView>(R.id.imageViewDropDownIcon).apply {
                setOnClickListener {
                    spinner.performClick()
                }
            }
        }

    }
}


