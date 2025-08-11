package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.presentation.shaders.SmartShaderView
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapp.util.DateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HumidityFragment : Fragment(R.layout.humidity_layout) {
    private lateinit var shaderView: SmartShaderView
    private lateinit var viewModel: WeatherAppViewModel

    private val dateHandle: DateHandle = DateHandle()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]
        shaderView = view.findViewById(R.id.shader_view)
        shaderView.setWaterColorFromResource(ContextCompat.getColor(context!!, R.color.md_theme_inversePrimary))
        CoroutineScope(Dispatchers.IO).launch {
            val text = view.findViewById<TextView>(R.id.humidityText)
            text.setText(viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity.toString())
            shaderView.setHumidity(viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity
                ?: 0)
            Log.d("HumidityFragment", "Humidity set to ${viewModel.getWeatherInfoByDate(dateHandle.getUnixTimestampString())?.humidity
                ?: 0}")
            Log.d("HumidityFragment", "Date for humidity check ${dateHandle.getUnixTimestampString()}")
        }
    }
}