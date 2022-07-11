package com.foltran.core_charts.adapters

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.foltran.core_charts.R
import com.foltran.core_charts.models.ChartDataModel
import com.foltran.core_charts.models.ChartDataModelPhotos
import com.foltran.core_charts.models.ChartTypes
import com.foltran.core_charts.models.toAAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAMarker
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import java.util.ArrayList


data class MessageModel(
    var category: String?,
    var x: Double?,
    var y: Double?
)

fun AAMoveOverEventMessageModel.toMessageModel() =
    MessageModel(category = this.category, x = this.x, y = this.y)

interface ChartCallback {
    fun chartViewMoveOverEventMessage(messageModel: MessageModel)
}

@BindingAdapter("chartDataModel")
fun View.setChartDataModel(chartDataModel: ChartDataModel?) {

    if (chartDataModel == null) return

    val aaChartModel =
        if (chartDataModel.chartType.toAAChartType() == AAChartType.Column) getAAChartModelForHighlightedHistogram(
            chartDataModel,
            context
        ) else getAAChartModelDefault(chartDataModel, context)

    val aaOptions = aaChartModel.aa_toAAOptions()

    val aaTooltip = AATooltip()
        .useHTML(true)
        .formatter(chartDataModel.yAxisFormatter)
        .valueDecimals(2)
        .backgroundColor("#000000")
        .borderColor("#000000")
        .style(
            AAStyle()
                .color("#FFD700")
                .fontSize(12f)
        )

    aaOptions.tooltip = aaTooltip


    //(this as AAChartView).aa_drawChartWithChartModel(aaChartModel)
    with(this as AAChartView) {
        aa_drawChartWithChartOptions(aaOptions)
    }

}

@BindingAdapter("chartDataModelPhotos", "chartCallbackSimple")
fun View.setSimple(
    chartDataModelPhotos: ChartDataModelPhotos?,
    chartCallbackSimple: ChartCallback?
) {

    if (chartCallbackSimple != null) {
        (this as AAChartView).callBack = object : AAChartView.AAChartViewCallBack {
            override fun chartViewMoveOverEventMessage(
                aaChartView: AAChartView,
                messageModel: AAMoveOverEventMessageModel
            ) {
                chartCallbackSimple.chartViewMoveOverEventMessage(messageModel = messageModel.toMessageModel())
            }

            override fun chartViewDidFinishLoad(aaChartView: AAChartView) {
            }
        }
    }

    if (chartDataModelPhotos != null) {
        val aaChartModel = getAAChartSimple(chartDataModelPhotos, context)
        val aaOptions = aaChartModel.aa_toAAOptions()

        val aaTooltip = AATooltip()
            .enabled(false)

        aaOptions.tooltip = aaTooltip

        (this as AAChartView).aa_drawChartWithChartOptions(aaOptions)
    }
}

fun getAAChartSimple(chartDataModel: ChartDataModelPhotos, context: Context): AAChartModel {

    val primaryColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.primary))

    return AAChartModel()
        .markerRadius(0f)
        .margin(arrayOf(0f, 0f, 0f, 0f))
        .chartType(ChartTypes.AREA.toAAChartType())
        .touchEventEnabled(true)
        .tooltipEnabled(true)
        .backgroundColor("#000000")
        .axesTextColor("#ffffff")
        .yAxisLabelsEnabled(false)
        .yAxisGridLineWidth(0.0f)
        .yAxisVisible(false)
        .xAxisVisible(false)
        .dataLabelsEnabled(false)
        .series(
            arrayOf(
                AASeriesElement()
                    .data(chartDataModel.vals.toTypedArray())
                    .color(primaryColor)
                    .showInLegend(false)
            )
        )
}

fun getAAChartModelDefault(chartDataModel: ChartDataModel, context: Context): AAChartModel {

    val primaryColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.primary))

    return AAChartModel()
        .chartType(chartDataModel.chartType.toAAChartType())
        .title(chartDataModel.title.orEmpty())
        .titleStyle(AAStyle().color("#ffffff"))
        .subtitleStyle(AAStyle().color("#ffffff"))
        .backgroundColor("#000000")
        .axesTextColor("#ffffff")
        .yAxisLabelsEnabled(false)
        .yAxisGridLineWidth(0.0f)
        .yAxisVisible(true)
        .dataLabelsEnabled(false)
        .series(
            arrayOf(
                AASeriesElement()
                    //.name("Berlin")
                    .data(chartDataModel.vals.toTypedArray())
                    .color(primaryColor)
                    .showInLegend(false)
            )
        )
}

fun getAAChartModelForHighlightedHistogram(
    chartDataModel: ChartDataModel,
    context: Context
): AAChartModel {

    val primaryColor = "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.primary))
    val secondaryColor =
        "#" + Integer.toHexString(ContextCompat.getColor(context, R.color.secondary))

    val valsNotHighlighted = chartDataModel.vals.subList(
        0,
        chartDataModel.valToHighlightHistogram!!
    ) + listOf(0.0) + chartDataModel.vals.subList(
        chartDataModel.valToHighlightHistogram + 1,
        chartDataModel.vals.size
    )
    val valsHighlighted = ArrayList<Double>().apply {
        for (i in 0..chartDataModel.vals.size) {
            if (i == chartDataModel.valToHighlightHistogram) this.add(chartDataModel.vals[i])
            else this.add(0.0)
        }
    }

    return AAChartModel()
        .chartType(chartDataModel.chartType.toAAChartType())
        .title(chartDataModel.title.orEmpty())
        .titleStyle(AAStyle().color("#ffffff"))
        .subtitleStyle(AAStyle().color("#ffffff"))
        .backgroundColor("#000000")
        .axesTextColor("#ffffff")
        .yAxisLabelsEnabled(false)
        .yAxisGridLineWidth(0.0f)
        .yAxisVisible(true)
        .dataLabelsEnabled(false)
        .stacking(AAChartStackingType.Normal)
        .series(
            arrayOf(
                AASeriesElement()
                    .data(valsHighlighted.toTypedArray())
                    .color(secondaryColor)
                    .showInLegend(false),
                AASeriesElement()
                    .data(valsNotHighlighted.toTypedArray())
                    .color(primaryColor)
                    .showInLegend(false)
            )
        )
}
