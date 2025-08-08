package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.presentation.adapter.WeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle



@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.location_layout) {

    private lateinit var viewModel: WeatherAppViewModel
    private lateinit var adapter: WeatherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]

        setupRecyclerView(view)
        observeWeatherData()
    }

    private fun setupRecyclerView(view: View) {
        adapter = WeatherAdapter(emptyList())
        val recyclerView = view.findViewById<RecyclerView>(R.id.weatherRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun observeWeatherData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allWeatherInfos.collect { weatherList ->
                    val filteredList = if (weatherList.isNotEmpty()) {
                        weatherList.drop(1) // Drop the first item
                    }else{
                        weatherList
                    }
                    adapter.updateData(filteredList)
                }
            }
        }
    }
}