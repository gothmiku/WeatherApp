package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherIconsSkewed
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import com.example.weatherapp.util.DateHandle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment(R.layout.current_weather_layout){

    private lateinit var viewModel: WeatherAppViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = DateHandle()

        viewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]

        val weatherIcon = view.findViewById<ImageView>(R.id.CurrentWeatherIcon)
        val weatherTitle = view.findViewById<TextView>(R.id.CurrentWeatherTitle)
        val weatherDesc = view.findViewById<TextView>(R.id.CurrentWeatherDesc)
        val weatherTemp = view.findViewById<TextView>(R.id.CurrentWeatherTemp)

        CoroutineScope(Dispatchers.IO).launch{
            val weatherInfo = viewModel.getWeatherInfoByDate(date.getUnixTimestampString())
            weatherIcon.setImageResource(WeatherIconsSkewed.fromApiDesc(weatherInfo?.weatherDescription ?: "").icon)
            weatherTitle.post{
                weatherTitle.text = weatherInfo?.weather
                weatherTitle.paint.textSkewX= -0.35f
                weatherTitle.invalidate()
            }
            weatherDesc.post{
                weatherDesc.text = weatherInfo?.weatherDescription
                weatherDesc.paint.textSkewX= -0.35f
                weatherDesc.invalidate()
            }
            weatherTemp.post{
                weatherTemp.text = weatherInfo?.temp?.toInt().toString()
                weatherTemp.paint.textSkewX= -0.35f
                weatherTemp.invalidate()
            }
        }
        weatherDesc.isSelected = true





    }
}