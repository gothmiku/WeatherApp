package com.example.weatherapp.presentation.shaders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class AGSLShaderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val shaderSource = """
        uniform float2 iResolution;
        uniform float iTime;
        uniform float4 iMouse;

        float fbm(float2 uv, float time) {
            float value = 0.0;
            // Properties
            float lacunarity = 1.5;
            float gain = 0.4;
            const int octaves = 4;
            //
            // Initial values
            float amplitude = 0.02;
            float frequency = 12.0;
            
            // loop of octaves
            for (int i = 0; i < octaves; i++) {
                value += amplitude * sin(uv.x * frequency + time);
                frequency *= lacunarity;
                amplitude *= gain;
            }
            
            return value;
        }

        half4 main(float2 fragCoord) {
            // Normalized pixel coordinates (from 0 to 1)
            float2 uv = fragCoord / iResolution;
            float3 col = float3(0.0);
            float water_limit = 0.3;
            float time = iTime * 1.5;
            
            bool click = iMouse.z > 0.0;
            if (click) {
                water_limit += sin(uv.x + time * 0.7) * 0.2;
            }
            
            water_limit += fbm(uv, time);
            
            col.b = 0.4 * smoothstep(water_limit, water_limit - (1.5 / iResolution.y), uv.y);
            
            return half4(col, 1.0);
        }
    """.trimIndent()

    private val paint = Paint()
    private val shader: RuntimeShader
    private val startTime = System.currentTimeMillis()

    init {
        shader = RuntimeShader(shaderSource)
        paint.shader = shader
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Update uniforms
        val currentTime = (System.currentTimeMillis() - startTime) / 1000f
        shader.setFloatUniform("iTime", currentTime)
        shader.setFloatUniform("iResolution", width.toFloat(), height.toFloat())
        shader.setFloatUniform("iMouse", 0f, 0f, 0f, 0f) // Default mouse state

        // Draw the shader
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Invalidate to keep animating
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shader.setFloatUniform("iResolution", w.toFloat(), h.toFloat())
    }
}