package com.foltran.feature_experience.chart.presentation

import android.os.Bundle
import android.view.LayoutInflater
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.foltran.core_charts.models.ChartDataModel
import com.foltran.core_utils.bases.BaseActivity
import com.foltran.feature_experience.chart.di.ExperienceChartModule
import com.foltran.feature_experience.databinding.ActivityExperienceChartBinding

class ExperienceChartActivity : BaseActivity(0, ExperienceChartModule.instance) {

    private lateinit var binding: ActivityExperienceChartBinding
    private val viewModel: ExperienceChartViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExperienceChartBinding.inflate(LayoutInflater.from(this))

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.chartDataModel = intent.getSerializableExtra(EXPERIENCE_CHART_COMMANDS) as ChartDataModel?
        setContentView(binding.root)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.state.observe(this){
            when(it) {
                ExperienceChartState.CloseActivity -> finish()
            }
        }
    }

    companion object {
        const val EXPERIENCE_CHART_COMMANDS = "experienceChartCommands"
    }

}