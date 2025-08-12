package com.example.weatherapp.presentation.ui.fragment

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.LAYER_TYPE_SOFTWARE
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.presentation.shaders.SmartShaderView
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import com.example.weatherapp.util.DateHandle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HumidityFragment : Fragment(R.layout.humidity_layout) {
    private lateinit var shaderView: SmartShaderView
    private lateinit var viewModel: WeatherAppViewModel

    private val dateHandle: DateHandle = DateHandle()

    fun applyNegativeColorFilter(textView: TextView) {
        val matrix = ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f, // Red
                0f, -1f, 0f, 0f, 255f, // Green
                0f, 0f, -1f, 0f, 255f, // Blue
                0f, 0f, 0f, 1f, 0f  // Alpha
            )
        )
        val filter = ColorMatrixColorFilter(matrix)
        textView.paint.colorFilter = filter
        textView.invalidate()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]
        shaderView = view.findViewById(R.id.shader_view)
        shaderView.setWaterColorFromResource(
            ContextCompat.getColor(
                context!!,
                R.color.md_theme_inversePrimary
            )
        )
        CoroutineScope(Dispatchers.IO).launch {
            val text = view.findViewById<TextView>(R.id.humidityText)
            text.setText(viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity.toString())

            shaderView.setHumidity(
                viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity
                    ?: 0
            )
            Log.d(
                "HumidityFragment", "Humidity set to ${
                    viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity
                        ?: 0
                }"
            )
            Log.d(
                "HumidityFragment",
                "Date for humidity check ${dateHandle.getUnixTimestampString()}"
            )
        }
        val textView = view.findViewById<TextView>(R.id.humidityText)
        textView.setLayerType(LAYER_TYPE_SOFTWARE, null)
        applyNegativeColorFilter(textView)

    }
}