package com.foltran.core_charts.models

import java.io.Serializable

class ChartDataModel(
    var chartType: ChartTypes = ChartTypes.AREA,
    val vals: List<Double>,
    val valToHighlightHistogram: Int? = 0,
    val title: String? = null,
    val yAxisFormatter: String = """
        function(){
        return '' + this.y;
        }
    """.trimIndent()
): Serializable


class ChartDataModelPhotos(
    var vals: List<Double>,
): Serializable
