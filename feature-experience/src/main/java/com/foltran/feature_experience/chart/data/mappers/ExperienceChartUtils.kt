package com.foltran.feature_experience.chart.data.mappers

import com.foltran.core_charts.models.ChartDataModel
import com.foltran.core_charts.models.ChartTypes
import com.foltran.feature_experience.chart.utils.*
import com.foltran.core_experience.shared.data.model.ExperienceStream

fun ExperienceStream?.toChartDataModel(position: Int) = ChartDataModel(
    chartType = ChartTypes.AREA,
    vals = when (position) {
        0 -> this?.velocitySmooth.mapTakeEveryIntervaled()
        1 -> this?.heartRate?.map{  it.toDouble()}.mapTakeEveryIntervaled()
        2 -> this?.cadence?.map{it.toDouble() * 2.0}.mapTakeEveryIntervaled()
        3 -> this?.altitude.mapTakeEveryIntervaled()
        else -> listOf()
    },
    yAxisFormatter = when (position) {
        0 -> paceJSFormatter()
        1 -> heartRateJSFormatter()
        2 -> cadenceRateJSFormatter()
        3 -> elevationRateJSFormatter()
        else -> ""
    },
)
