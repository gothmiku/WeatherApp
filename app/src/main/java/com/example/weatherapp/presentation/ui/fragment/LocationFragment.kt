package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.R
import com.example.weatherapp.presentation.viewmodel.GPSViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.coordinates_layout){

    private lateinit var viewModel : GPSViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GPSViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch{
            val coordinate = viewModel.getLatestsGPSInfo()
            val latitudeText = view.findViewById<TextView>(R.id.latText)
            val longtitudeText = view.findViewById<TextView>(R.id.lonText)
            latitudeText.text = coordinate?.lat.toString()
            longtitudeText.text = coordinate?.lon.toString()
        }
    }
}