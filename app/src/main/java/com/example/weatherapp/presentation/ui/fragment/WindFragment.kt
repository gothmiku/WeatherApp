package com.example.weatherapp.presentation.ui.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.weatherapp.R
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import com.example.weatherapp.util.DateHandle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

@AndroidEntryPoint
class WindFragment : Fragment(R.layout.wind_layout) {

    private lateinit var viewModel : WeatherAppViewModel

    val date = DateHandle()

    private var smoothRotationAnimator: ObjectAnimator? = null
    private var turbulentAnimator: ObjectAnimator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]
        val windSpeedText = view.findViewById<TextView>(R.id.windSpeedText)
        val windDegIcon = view.findViewById<ImageView>(R.id.compassIcon)


        lifecycleScope.launch{
            val weatherInfo = withContext(Dispatchers.IO){
                viewModel.getOldestWeatherInfo()
            }
            windSpeedText.text = weatherInfo?.wind_speed.toString()
            windDegIcon.rotation = 180f + weatherInfo?.wind_deg?.toFloat()!!

            startSmoothWiggle(windDegIcon, 180f + weatherInfo.wind_deg?.toFloat()!!, weatherInfo.wind_speed ?: 0f)
            startTurbulentEffect(windDegIcon)
        }





    }

    private fun startSmoothWiggle(icon: ImageView, baseRotation: Float, windSpeed: Float) {
        // Calculate wiggle amplitude based on wind speed
        val wiggleAmplitude = (windSpeed * 2f).coerceIn(-10f, 10f)

        smoothRotationAnimator = ObjectAnimator.ofFloat(
            icon, "rotation",
            baseRotation - wiggleAmplitude,
            baseRotation + wiggleAmplitude
        ).apply {
            duration = (2000 + (20f - windSpeed) * 100).toLong() // Faster wiggle for higher wind
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun startTurbulentEffect(icon: ImageView) {
        fun createTurbulentRotation() {
            val currentRotation = icon.rotation
            val turbulentOffset = Random.nextFloat() * 30f - 15f

            turbulentAnimator = ObjectAnimator.ofFloat(
                icon, "rotation",
                currentRotation,
                currentRotation + turbulentOffset,
                currentRotation
            ).apply {
                duration = (200..500).random().toLong()
                interpolator = DecelerateInterpolator()

                doOnEnd {
                    // Schedule next turbulent effect randomly
                    icon.postDelayed({
                        if (isAdded) createTurbulentRotation()
                    }, (1000..4000).random().toLong())
                }
                start()
            }
        }

        // Start first turbulent effect after a delay
        icon.postDelayed({
            if (isAdded) createTurbulentRotation()
        }, (2000..5000).random().toLong())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        smoothRotationAnimator?.cancel()
        turbulentAnimator?.cancel()
    }
}