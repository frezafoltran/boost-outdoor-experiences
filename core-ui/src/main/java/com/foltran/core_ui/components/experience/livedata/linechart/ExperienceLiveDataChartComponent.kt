package com.foltran.core_ui.components.experience.livedata.linechart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.foltran.core_ui.R


class ExperienceLiveDataChartComponent @JvmOverloads constructor(context: Context,
                                                                 attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var dataPoints: List<Double> = listOf()
    private var maxPoint: Double = 0.0
    private var minPoint: Double = 0.0
    private val rangeOfPoints get() = maxPoint - minPoint

    private var xIncrement: Double = 0.0

    var time: Long = (0).toLong()

    private val strokeWidth = 8f
    private val strokeWidthCircle = 5f

    private var paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.secondary_variant)
        strokeWidth = strokeWidth
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private var circlePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.secondary_variant)
        strokeWidth = strokeWidthCircle
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var polygonPaint = Paint()
    private val polygonPath = Path()

    private var lastRefreshAt: Long? = null
    private var lastPaintedIndex: Int = -1

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawLineTillPoint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        setupPolygonPaint(h.toFloat())
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    private fun setupPolygonPaint(height: Float? = null) {

        val shader: Shader = LinearGradient(
            0f,
            0f,
            0f,
            height ?: 10f, //change this to height of canvas,
            ContextCompat.getColor(context, R.color.secondary_variant),
            ContextCompat.getColor(context, R.color.primary_dark),
            Shader.TileMode.CLAMP
        )

        polygonPaint.also {
            it.color = ContextCompat.getColor(context, R.color.secondary_variant)
            it.strokeWidth = strokeWidthCircle
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
            it.shader = shader
        }
    }

    fun setDataPoints(dataPoints: List<Double>) {
        this.dataPoints = dataPoints
        this.maxPoint = dataPoints.maxOrNull() ?: 0.0
        this.minPoint = dataPoints.minOrNull() ?: 0.0
    }

    fun setTime(phase: Float) {

        val curTime = System.currentTimeMillis()
        if (lastRefreshAt == null || lastRefreshAt ?: curTime < curTime - MAX_REFRESH_RATE_MS) {
            lastRefreshAt = curTime
            lastPaintedIndex = (phase * dataPoints.size).toInt()
            invalidate()
        }
    }

    private fun Canvas.getScaledPoint(value: Double): Double {
        return (height - strokeWidth - radius).toDouble() * (1 - ((value - minPoint) / rangeOfPoints)) + strokeWidth.toDouble() + radius
    }

    private fun Canvas.drawPolygon(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        x3: Float,
        y3: Float,
        x4: Float,
        y4: Float
    ) {

        with (polygonPath) {
            reset()
            moveTo(x1, y1)
            lineTo(x2, y2)
            lineTo(x3, y3)
            lineTo(x4, y4)
        }

        drawPath(polygonPath, polygonPaint)
    }

    private fun Canvas.drawLineTillPoint(shadeBottom: Boolean = true) {

        if (lastPaintedIndex >= dataPoints.size || lastPaintedIndex < 1) return

        val canvasHeight = (this.height).toFloat()

        xIncrement = (this.width.toDouble() - radius)/ lastPaintedIndex
        this.drawColor(ContextCompat.getColor(context, R.color.primary_dark))

        var previousPointX = 0.0.toFloat()
        var previousPointY = getScaledPoint(dataPoints[0]).toFloat()


        for (point in dataPoints.subList(1, lastPaintedIndex + 1)) {
            val curPointY = getScaledPoint(point).toFloat()
            val curPointX = (previousPointX + xIncrement).toFloat()


            drawLine(
                previousPointX,
                previousPointY,
                curPointX,
                curPointY,
                paint
            )

            if (shadeBottom) {
                drawPolygon(
                    previousPointX,
                    canvasHeight,
                    curPointX,
                    canvasHeight,
                    curPointX,
                    curPointY,
                    previousPointX,
                    previousPointY
                )
            }

            previousPointX = curPointX
            previousPointY = curPointY
        }
        drawCircle()
    }

    private fun Canvas.drawCircle() {
        val endPoint = dataPoints[lastPaintedIndex]
        val centerY = (this.getScaledPoint(endPoint)).toFloat()
        val centerX = (xIncrement * lastPaintedIndex).toFloat()

        this.drawCircle(centerX, centerY, radius, circlePaint)
    }

    companion object {
        const val radius = 20f
        const val MAX_REFRESH_RATE_MS = 500
    }
}
