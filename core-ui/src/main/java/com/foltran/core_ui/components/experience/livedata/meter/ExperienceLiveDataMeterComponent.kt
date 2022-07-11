package com.foltran.core_ui.components.experience.livedata.meter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.foltran.core_ui.R
import com.foltran.core_ui.components.experience.livedata.linechart.ExperienceLiveDataChartComponent
import kotlin.math.sqrt

class ExperienceLiveDataMeterComponent @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val strokeWidth = 20f
    private var paint = Paint()
    private val basisGradientPath = Path()

    private var indicatorPaint = Paint()
    private val indicatorPath = Path()
    private val indicatorStrokeWidth = 10f
    private val indicatorHeight = 60f

    private var diamondPaint = Paint()
    private val diamondStrokeWidth = 10f
    private val diamondPath = Path()
    private val diamondSide = 15f
    private val diamondDiagonal = (sqrt(2.0) * diamondSide).toFloat()

    private var lastRefreshAt: Long? = null
    private var curRatio: Double = -1.0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawGradientBasis()
        canvas?.drawCurIndicator()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupPaint(w.toFloat())
        setupIndicatorPaint()
    }

    fun setTime(curRatio: Double) {

        val curTime = System.currentTimeMillis()
        if (lastRefreshAt == null || lastRefreshAt ?: curTime < curTime - MAX_REFRESH_RATE_MS) {
            lastRefreshAt = curTime
            this.curRatio = curRatio
            invalidate()
        }
    }

    private fun setupPaint(width: Float) {

        val shader: Shader = LinearGradient(
            0f,
            0f,
            width,
            0f,
            ContextCompat.getColor(context, R.color.red_basis),
            ContextCompat.getColor(context, R.color.green_basis),
            Shader.TileMode.CLAMP
        )


        paint.also {
            it.strokeWidth = strokeWidth
            it.isAntiAlias = true
            it.style = Paint.Style.STROKE
            it.shader = shader
        }
    }

    private fun setupIndicatorPaint() {

        indicatorPaint.also {
            it.strokeWidth = indicatorStrokeWidth
            it.isAntiAlias = true
            it.style = Paint.Style.STROKE
            it.color = ContextCompat.getColor(context, R.color.white)
        }

        diamondPaint.also {
            it.strokeWidth = diamondStrokeWidth
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
            it.color = ContextCompat.getColor(context, R.color.white)
        }
    }

    private fun Canvas.drawGradientBasis() {

        val halfHeight = height.toFloat() / 2f

        with (basisGradientPath) {
            reset()
            moveTo(0f, halfHeight)
            lineTo(width.toFloat(), halfHeight)
        }

        drawPath(basisGradientPath, paint)
    }

    private fun Canvas.drawCurIndicator() {

        if (curRatio < 0 || curRatio > 1) return

        val halfHeight = height.toFloat() / 2f

        val xPos = (width.toDouble() * curRatio).toFloat()
        val bottomTipOfDimamond = halfHeight - indicatorHeight/2f

        with (indicatorPath) {
            reset()
            moveTo(xPos, halfHeight + indicatorHeight/2f)
            lineTo(xPos, bottomTipOfDimamond)
        }
        drawPath(indicatorPath, indicatorPaint)

        with (diamondPath) {
            reset()
            lineTo(xPos, bottomTipOfDimamond)
            lineTo(xPos - diamondSide, bottomTipOfDimamond - diamondSide)
            lineTo(xPos, bottomTipOfDimamond - (diamondDiagonal * 2))
            lineTo(xPos + diamondSide, bottomTipOfDimamond- diamondSide)
            lineTo(xPos, bottomTipOfDimamond)
        }

        drawPath(diamondPath, diamondPaint)
    }

    companion object {
        const val MAX_REFRESH_RATE_MS = 100
    }
}