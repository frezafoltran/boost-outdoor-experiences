package com.foltran.feature_experience.details.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.foltran.core_charts.models.ChartDataModel
import com.foltran.core_charts.models.ChartTypes
import com.foltran.feature_experience.chart.data.mappers.toChartDataModel
import com.foltran.core_experience.shared.data.model.ExperienceStream
import com.foltran.feature_experience.core.presentation.chart.ExperienceChartCommands


data class ExperienceDetailsChartSpinnerUIModel(
    val onClickExpandGraph: () -> Unit,
    val spinnerCategories: List<String>,
    val chartType: ChartTypes = ChartTypes.AREA
) {

    private val spinnerPosition: MutableLiveData<Int> = MutableLiveData(0)
    val experienceStream: MutableLiveData<ExperienceStream?> = MutableLiveData(null)

    val chartData: LiveData<ChartDataModel> = MediatorLiveData<ChartDataModel>().apply {
        addSource(experienceStream) { stream ->
            if (chartType == ChartTypes.AREA) {
                value = stream.toChartDataModel(spinnerPosition.value ?: 0).also {
                    it.chartType = chartType
                }
            }
            else {
                value = ChartDataModel(
                    chartType = chartType,
                    vals = listOf(2.0, 1.0, 3.0, 4.0, 1.0),
                    valToHighlightHistogram = 2
                )
            }
        }
        addSource(spinnerPosition) {
            if (chartType == ChartTypes.AREA) {
                value = experienceStream.value.toChartDataModel(it).also { chartData ->
                    chartData.chartType = chartType
                }
            }
            else {
                value = ChartDataModel(
                    chartType = chartType,
                    vals = listOf(2.0, 1.0, 3.0, 4.0, 1.0),
                    valToHighlightHistogram = 2
                )
            }
        }
    }

    val chartDataCommands = ExperienceChartCommands(
            onClickExpandGraph = onClickExpandGraph,
            spinnerCategories = spinnerCategories,
            onSelectCategory = { index ->
                spinnerPosition.value = index
            }
        )


}