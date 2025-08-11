package com.example.weatherapp.presentation.shaders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class AGSLShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Very simple AGSL shader - minimal complexity
    private val shaderSource = """
        uniform vec2 iResolution;
        uniform float iTime;
        uniform vec3 waterColor;
        uniform float humidity;

        vec4 main(vec2 fragCoord) {
            vec2 uv = fragCoord / iResolution;
            float baseLevel = 0.9 - (humidity / 100.0) * 0.8;
            // Simple animated water level
            float wave = 0.02 * sin(uv.x * 8.0 + iTime * 1.5);
            float waterLevel = baseLevel + wave;
            
            // Water appears below the level (bottom portion)
            if (uv.y > waterLevel) {
                return vec4(waterColor, 0.7);
            } else {
                return vec4(0.0, 0.0, 0.0, 0.0);
            }
        }
    """

    private val paint = Paint()
    private var shader: RuntimeShader? = null
    private val startTime = System.currentTimeMillis()
    private var hasShaderError = false

    init {
        try {
            Log.d("AGSLShaderView", "Creating RuntimeShader with source: ${shaderSource.take(100)}...")
            shader = RuntimeShader(shaderSource)
            paint.shader = shader

            // Set initial uniforms
            shader?.setFloatUniform("iResolution", 100f, 100f) // Temporary values
            shader?.setFloatUniform("iTime", 0f)
            setWaterColor(0.0f, 0.4f, 0.8f) // Blue water

            Log.d("AGSLShaderView", "AGSL Shader initialized successfully")
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Failed to initialize AGSL shader", e)
            hasShaderError = true
            // Use blue fallback instead of purple
            paint.color = android.graphics.Color.parseColor("#660099FF")
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (hasShaderError || shader == null) {
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            return
        }

        try {
            val currentTime = (System.currentTimeMillis() - startTime) / 1000f
            shader?.setFloatUniform("iTime", currentTime)
            shader?.setFloatUniform("iResolution", width.toFloat(), height.toFloat())

            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            invalidate()
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error during AGSL drawing", e)
            hasShaderError = true
            paint.shader = null
            // Use blue fallback instead of purple
            paint.color = android.graphics.Color.parseColor("#660099FF")
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        try {
            shader?.setFloatUniform("iResolution", w.toFloat(), h.toFloat())
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error setting AGSL resolution", e)
        }
    }

    fun setWaterColor(red: Float, green: Float, blue: Float) {
        try {
            shader?.setFloatUniform("waterColor", red, green, blue)
            invalidate()
            Log.d("AGSLShaderView", "Set water color: R=$red, G=$green, B=$blue")
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error setting AGSL color", e)
            paint.color = android.graphics.Color.argb(
                180, // 70% alpha
                (red * 255).toInt().coerceIn(0, 255),
                (green * 255).toInt().coerceIn(0, 255),
                (blue * 255).toInt().coerceIn(0, 255)
            )
            invalidate()
        }
    }

    fun setWaterColorFromResource(colorResId: Int) {
        try {
            // Check if the resource exists first
            val color = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getColor(colorResId)
            } else {
                @Suppress("DEPRECATION")
                context.resources.getColor(colorResId)
            }

            val red = android.graphics.Color.red(color) / 255f
            val green = android.graphics.Color.green(color) / 255f
            val blue = android.graphics.Color.blue(color) / 255f

            Log.d("AGSLShaderView", "Setting color from resource $colorResId: #${Integer.toHexString(color)}")
            setWaterColor(red, green, blue)
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error setting AGSL color from resource $colorResId", e)
            // Fallback to default blue
            setWaterColor(0.0f, 0.4f, 0.8f)
        }
    }

    fun setHumidity(humidityPercent: Int) {
        val clampedHumidity = humidityPercent.coerceIn(0, 100)
        try {
            shader?.setFloatUniform("humidity", clampedHumidity.toFloat())
            invalidate()
            Log.d("AGSLShaderView", "Set humidity: $clampedHumidity%")
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error setting humidity", e)
        }
    }

    fun setHumidity(humidityPercent: Float) {
        val clampedHumidity = humidityPercent.coerceIn(0f, 100f)
        try {
            shader?.setFloatUniform("humidity", clampedHumidity)
            invalidate()
            Log.d("AGSLShaderView", "Set humidity: $clampedHumidity%")
        } catch (e: Exception) {
            Log.e("AGSLShaderView", "Error setting humidity", e)
        }
    }
}