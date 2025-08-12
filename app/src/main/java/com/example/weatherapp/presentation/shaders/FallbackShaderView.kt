package com.example.weatherapp.presentation.shaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class FallbackShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var humidity = 50f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val startTime = System.currentTimeMillis()

    init {
        // Color options - change this line to modify the color:

        paint.color = Color.parseColor("#0066CC") // Original blue
        // paint.color = Color.parseColor("#00CC99") // Cyan
        // paint.color = Color.parseColor("#00CC00") // Green
        // paint.color = Color.parseColor("#9900CC") // Purple
        // paint.color = Color.parseColor("#FF6600") // Orange
        // paint.color = Color.parseColor("#FF3399") // Pink
        // paint.color = Color.parseColor("#3399FF") // Light blue

        // Or use Color.rgb() method:
        // paint.color = Color.rgb(51, 153, 255) // Light blue (RGB: 51, 153, 255)
    }

    fun setColor(color: Int) {
        paint.color = color
        invalidate()
    }

    fun setColorFromResource(colorResId: Int) {
        paint.color = context.getColor(colorResId)
        invalidate()
    }

    fun setHumidity(humidityPercent: Int) {
        humidity = humidityPercent.coerceIn(0, 100).toFloat()
        invalidate()
    }

    fun setHumidity(humidityPercent: Float) {
        humidity = humidityPercent.coerceIn(0f, 100f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val currentTime = (System.currentTimeMillis() - startTime) / 1000f
        val w = width.toFloat()
        val h = height.toFloat()

        // Create a simple animated wave effect as fallback
        val path = Path()
        path.moveTo(0f, h)

        for (x in 0..width step 5) {
            val normalizedX = x / w
            val waveY = h * 0.7f + sin(normalizedX * 10f + currentTime * 2f) * h * 0.1f
            path.lineTo(x.toFloat(), waveY)
        }

        path.lineTo(w, h)
        path.lineTo(0f, h)
        path.close()

        // Set alpha for transparency
        paint.alpha = (0.4f * 255).toInt()
        canvas.drawPath(path, paint)

        invalidate()
    }
}