package com.example.weatherapp.presentation.shaders

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.weatherapp.presentation.shaders.AGSLShaderView
import com.example.weatherapp.presentation.shaders.FallbackShaderView

class SmartShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val shaderView = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Use AGSL for Android 13+
            AGSLShaderView(context, attrs, defStyleAttr)
        } else {
            // Use fallback for older versions
            FallbackShaderView(context, attrs, defStyleAttr)
        }

        addView(shaderView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }
}