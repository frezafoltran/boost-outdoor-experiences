package com.foltran.feature_experience.core.presentation.chart

import com.foltran.core_charts.models.ChartDataModel
import com.foltran.core_charts.models.ChartTypes
import com.foltran.core_experience.shared.data.model.ExperienceStream


val tempFakeDataY = listOf<Double>(
    5.0,
    4.0,
    5.5,
    6.0,
    7.5,
    8.0,
    7.0,
    4.0,
    2.0
)


fun ExperienceStream?.toComparisonChartDataModel(position: Int) = ChartDataModel(
    chartType = ChartTypes.COLUMN,
    vals = tempFakeDataY,
    valToHighlightHistogram = 3
)
