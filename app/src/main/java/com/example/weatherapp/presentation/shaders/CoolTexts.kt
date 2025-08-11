package com.example.weatherapp.presentation.shaders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.ui.graphics.LinearGradient

class SkewedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val skewX = -0.35f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val textHeight = paint.textSize
        val additionalWidth = (textHeight * Math.abs(skewX)).toInt()

        setMeasuredDimension(
            measuredWidth + additionalWidth,
            measuredHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        // IMPORTANT: Reset paint skew first
        paint.textSkewX = 0f

        canvas?.save()

        val textHeight = paint.textSize
        val skewOffset = textHeight * Math.abs(skewX)
        canvas?.translate(skewOffset, 0f)
        canvas?.skew(skewX, 0f)

        super.onDraw(canvas)
        canvas?.restore()
    }
}

// 1. Glowing Temperature Text
class GlowingTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        // Create glow effect
        paint.setShadowLayer(15f, 0f, 0f, Color.parseColor("#FF6B35")) // Orange glow
        paint.color = Color.WHITE
        super.onDraw(canvas)
    }
}

// 2. Gradient Temperature Text
class GradientTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val gradient = android.graphics.LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(
                Color.parseColor("#FF6B35"), // Hot orange
                Color.parseColor("#F7931E"), // Warm yellow
                Color.parseColor("#FFD23F")  // Bright yellow
            ),
            null,
            Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        super.onDraw(canvas)
    }
}

// 3. Outlined Temperature Text
class OutlinedTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val currentTextColor = currentTextColor

        // Draw outline
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f
        paint.color = Color.parseColor("#2C3E50") // Dark blue outline
        super.onDraw(canvas)

        // Draw fill
        paint.style = Paint.Style.FILL
        paint.color = currentTextColor
        super.onDraw(canvas)
    }
}

// 4. 3D Shadow Temperature Text
class Shadow3DTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        val originalColor = currentTextColor

        // Draw multiple shadow layers for 3D effect
        for (i in 6 downTo 1) {
            paint.color = Color.argb(50, 0, 0, 0)
            canvas.save()
            canvas.translate(i.toFloat(), i.toFloat())
            super.onDraw(canvas)
            canvas.restore()
        }

        // Draw main text
        paint.color = originalColor
        super.onDraw(canvas)
    }
}

// 5. Neon Temperature Text
class NeonTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        // Outer glow
        paint.setShadowLayer(25f, 0f, 0f, Color.parseColor("#00FFFF"))
        paint.color = Color.parseColor("#00FFFF")
        super.onDraw(canvas)

        // Inner glow
        paint.setShadowLayer(10f, 0f, 0f, Color.parseColor("#FFFFFF"))
        paint.color = Color.parseColor("#E0FFFF")
        super.onDraw(canvas)
    }
}

// 6. Animated Pulsing Temperature (requires animation)
class PulsingTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var animator: ValueAnimator? = null
    private var glowRadius = 10f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        startPulseAnimation()
    }

    private fun startPulseAnimation() {
        animator = ValueAnimator.ofFloat(5f, 25f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { animation ->
                glowRadius = animation.animatedValue as Float
                invalidate()
            }
        }
        animator?.start()
    }

    override fun onDraw(canvas: Canvas) {
        val value = this.text.toString().toInt()
        if (value > 40) {
            paint.setShadowLayer(glowRadius, 0f, 0f, Color.parseColor("#f02e2e"))
        }
        else if (value > 30) {
            paint.setShadowLayer(glowRadius, 0f, 0f, Color.parseColor("#e6a12c"))
        }
        else if (value > 20) {
            paint.setShadowLayer(glowRadius, 0f, 0f, Color.parseColor("#2ef08f"))
        }
        else if(value > 10){
            paint.setShadowLayer(glowRadius, 0f, 0f, Color.parseColor("#2cdae6"))
        }
        else{
            paint.setShadowLayer(glowRadius, 0f, 0f, Color.parseColor("#2748d9"))
        } // I know its not efficient but idc its faster to write
        paint.color = Color.WHITE
        super.onDraw(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }
}

// 7. Textured Temperature Text (using pattern)
class TexturedTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        // Create a radial gradient for texture
        val shader = RadialGradient(
            width / 2f, height / 2f, width / 2f,
            intArrayOf(
                Color.parseColor("#FFD700"), // Gold center
                Color.parseColor("#FFA500"), // Orange middle
                Color.parseColor("#FF4500")  // Red outer
            ),
            floatArrayOf(0f, 0.7f, 1f),
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        super.onDraw(canvas)
    }
}

// 8. Skewed + Glowing Temperature (combining your original request)
class SkewedGlowTemperatureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        canvas?.save()

        // Apply skew with offset compensation
        val textHeight = paint.textSize
        val skewOffset = textHeight * 0.35f
        canvas?.translate(skewOffset, 0f)
        canvas?.skew(-0.35f, 0f)

        // Add glow effect
        paint.setShadowLayer(20f, 0f, 0f, Color.parseColor("#FF6B35"))
        paint.color = Color.WHITE

        super.onDraw(canvas)
        canvas?.restore()
    }
}

// Usage example in your activity/fragment:
// Replace your temperature TextView with any of these:

/*
// In your XML layout:
<com.yourpackage.GlowingTemperatureView
    android:id="@+id/CurrentWeatherTemp"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textSize="48sp"
    android:textStyle="bold" />

// In your Kotlin code (same as before):
val weatherTemp = view.findViewById<TextView>(R.id.CurrentWeatherTemp)
weatherTemp.post {
    weatherTemp.text = "${weatherInfo?.temp?.toInt()}°"
}
*/