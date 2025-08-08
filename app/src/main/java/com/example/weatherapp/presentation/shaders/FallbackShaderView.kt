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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val startTime = System.currentTimeMillis()

    init {
        paint.color = Color.parseColor("#0066CC") // Blue color for water effect
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