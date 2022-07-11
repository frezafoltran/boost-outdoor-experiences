package com.foltran.core_charts.models

import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType

enum class ChartTypes {
    AREA,
    BAR,
    PIE,
    COLUMN
}

fun ChartTypes.toAAChartType() = when(this){
    ChartTypes.AREA -> AAChartType.Area
    ChartTypes.BAR -> AAChartType.Bar
    ChartTypes.COLUMN -> AAChartType.Column
    ChartTypes.PIE -> AAChartType.Pie
}