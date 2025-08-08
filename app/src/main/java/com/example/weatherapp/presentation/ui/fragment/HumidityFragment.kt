package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.presentation.shaders.SmartShaderView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HumidityFragment : Fragment(R.layout.humidity_layout) {
    private lateinit var shaderView: SmartShaderView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shaderView = view.findViewById(R.id.shader_view)
    }
}