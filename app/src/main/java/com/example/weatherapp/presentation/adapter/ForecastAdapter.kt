package com.example.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherIcons
import com.example.weatherapp.data.model.WeatherInfo
import java.text.SimpleDateFormat
import java.util.Locale


class WeatherAdapter(
    private var weatherList: List<WeatherInfo>
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    fun updateData(newWeatherList: List<WeatherInfo>) {
        weatherList = newWeatherList
        notifyDataSetChanged() // This tells the RecyclerView to refresh all items
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.weatherIcon)
        val name: TextView = view.findViewById(R.id.weatherText)
        val desc: TextView = view.findViewById(R.id.weatherDescriptionText)
        val temp: TextView = view.findViewById(R.id.tempText)
        val day: TextView = view.findViewById(R.id.dayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_item_layout, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val apiWeatherName = weatherList[position]
        val weatherType = WeatherIcons.fromApiDesc(apiWeatherName.weatherDescription)

        val dayOfTheWeek = (apiWeatherName.date.toLong() * 1000L)
        val sdf = SimpleDateFormat("EEE", Locale.ENGLISH)
        val date = java.util.Date(dayOfTheWeek)
        val dayOfWeek = sdf.format(date)


        holder.icon.setImageResource(weatherType.icon)
        holder.name.text = apiWeatherName.weather
        holder.desc.text = apiWeatherName.weatherDescription
        holder.temp.text = apiWeatherName.temp.toInt().toString()
        holder.day.text = dayOfWeek
    }

    override fun getItemCount() = weatherList.size
}