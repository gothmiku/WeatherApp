package com.example.weatherapp.presentation.shaders

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.annotation.ColorRes

class SmartShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val shaderView: Any
    private var useAGSL = false

    init {
        // For now, let's prefer the fallback view for stability
        // You can change this to true to test AGSL when ready
        val shouldUseAGSL = true && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

        shaderView = if (shouldUseAGSL) {
            try {
                Log.d("SmartShaderView", "Attempting to use AGSL")
                useAGSL = true
                AGSLShaderView(context, attrs, defStyleAttr)
            } catch (e: Exception) {
                Log.e("SmartShaderView", "AGSL failed, falling back", e)
                useAGSL = false
                FallbackShaderView(context, attrs, defStyleAttr)
            }
        } else {
            Log.d("SmartShaderView", "Using fallback view")
            useAGSL = false
            FallbackShaderView(context, attrs, defStyleAttr)
        }

        addView(shaderView as android.view.View, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun setWaterColorFromResource(@ColorRes colorResId: Int) {
        try {
            when (shaderView) {
                is AGSLShaderView -> {
                    if (useAGSL && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        shaderView.setWaterColorFromResource(colorResId)
                    }
                }
                is FallbackShaderView -> {
                    shaderView.setColorFromResource(colorResId)
                }
            }
        } catch (e: Exception) {
            Log.e("SmartShaderView", "Error setting color from resource", e)
        }
    }

    fun setHumidity(humidityPercent: Int) {
        try {
            when (shaderView) {
                is AGSLShaderView -> {
                    if (useAGSL && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        shaderView.setHumidity(humidityPercent)
                    }
                }
                is FallbackShaderView -> {
                    shaderView.setHumidity(humidityPercent)
                }
            }
        } catch (e: Exception) {
            Log.e("SmartShaderView", "Error setting humidity", e)
        }
    }

    fun setHumidity(humidityPercent: Float) {
        try {
            when (shaderView) {
                is AGSLShaderView -> {
                    if (useAGSL && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        shaderView.setHumidity(humidityPercent)
                    }
                }
                is FallbackShaderView -> {
                    shaderView.setHumidity(humidityPercent)
                }
            }
        } catch (e: Exception) {
            Log.e("SmartShaderView", "Error setting humidity", e)
        }
    }

    fun setWaterColor(red: Float, green: Float, blue: Float) {
        try {
            when (shaderView) {
                is AGSLShaderView -> {
                    if (useAGSL && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        shaderView.setWaterColor(red, green, blue)
                    }
                }
                is FallbackShaderView -> {
                    val color = android.graphics.Color.rgb(
                        (red * 255).toInt().coerceIn(0, 255),
                        (green * 255).toInt().coerceIn(0, 255),
                        (blue * 255).toInt().coerceIn(0, 255)
                    )
                    shaderView.setColor(color)
                }
            }
        } catch (e: Exception) {
            Log.e("SmartShaderView", "Error setting color", e)
        }
    }
}