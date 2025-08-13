package com.example.weatherapp.presentation.ui.fragment

import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.model.AddressResponseCache
import com.example.weatherapp.data.repo.CacheRepo
import com.example.weatherapp.presentation.viewmodel.GPSViewModel
import com.example.weatherapp.presentation.viewmodel.OpenMapViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Cache
import javax.inject.Inject

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.coordinates_layout){

    private lateinit var viewModel : GPSViewModel
    private lateinit var gpsResponseViewModel : OpenMapViewModel
    @Inject
    lateinit var cacheRepo : CacheRepo

    fun checkAndFillForCache(){
        viewModel = ViewModelProvider(this)[GPSViewModel::class.java]
        gpsResponseViewModel = ViewModelProvider(this)[OpenMapViewModel::class.java]

        lifecycleScope.launch {
            val coordinate = withContext(Dispatchers.IO) {
                viewModel.getLatestsGPSInfo()
            }
            if(cacheRepo.getAddressCacheCount()>=1){
                Log.d("Caching","Cache exists!")
                if(cacheRepo.haversine(cacheRepo.getLatestAddressCache()?.lat?: 30f
                ,cacheRepo.getLatestAddressCache()?.lon ?: 30f,coordinate?.lat ?: 30f,coordinate?.lon?: 30f)>=5f){
                    Log.d("Caching","Distance between cache and current coordinates" +
                            "${cacheRepo.haversine(cacheRepo.getLatestAddressCache()?.lat?: 30f
                                ,cacheRepo.getLatestAddressCache()?.lon ?: 30f,coordinate?.lat ?: 30f,coordinate?.lon?: 30f)} " + " km")
                    cacheRepo.deleteAllAddressCaches()
                    Log.d("Caching","Cache deleted")
                    Log.d("Caching","Fetching new data...")
                    val response = withContext(Dispatchers.IO){
                        gpsResponseViewModel.getJSON(coordinate?.lat ?: 40f, coordinate?.lon ?: 29f)
                    }
                    if(response!=null){
                        Log.d("Caching","New data fetched")
                    }else{
                        Log.d("Caching","No new data fetched")
                    }
                    cacheRepo.insertAddressCache(AddressResponseCache(city = response.address.city,country = response.address.country,countryCode = response.address.countryCode,lat=coordinate?.lat ?: 40f,lon=coordinate?.lon ?: 24f))
                    Log.d("Caching","New data inserted into cache")
                }
            }else{
                Log.d("Caching","No cache detected... \nFetching new data...")
                val response = withContext(Dispatchers.IO){
                    gpsResponseViewModel.getJSON(coordinate?.lat ?: 40f, coordinate?.lon ?: 29f)
                }
                if(response!=null){
                    Log.d("Caching","New data has been successfully fetched")
                }else{
                    Log.d("Caching","Failed to fetch new data")
                }
                Log.d("Caching","Inserting new data into cache")
                cacheRepo.insertAddressCache(AddressResponseCache(city = response.address.city
                    ,country = response.address.country
                    ,countryCode = response.address.countryCode
                    ,lat=coordinate?.lat ?: 40f
                    ,lon=coordinate?.lon ?: 24f))
                Log.d("Caching","New data inserted into cache")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[GPSViewModel::class.java]
        gpsResponseViewModel = ViewModelProvider(this)[OpenMapViewModel::class.java]

        checkAndFillForCache()

        // Find views first
        val latitudeText = view.findViewById<TextView>(R.id.latText)
        val longtitudeText = view.findViewById<TextView>(R.id.lonText)
        val cityText = view.findViewById<TextView>(R.id.cityText)
        val countryText = view.findViewById<TextView>(R.id.countryText)

        // Use lifecycleScope instead of CoroutineScope
        lifecycleScope.launch {
            // Do background work
            val coordinate = withContext(Dispatchers.IO) {
                viewModel.getLatestsGPSInfo()
            }

            coordinate?.let {


                latitudeText.text = it.lat.toString()
                longtitudeText.text = it.lon.toString()
            }
            val cache = withContext(Dispatchers.IO)
            { cacheRepo.getLatestAddressCache() }
            cityText.text = cache?.city ?: "Loading..."
            countryText.text = cache?.countryCode?.uppercase() ?: "Loading..."
        }
        cityText.isSelected=true
        cityText.setHorizontallyScrolling(true)
    }
}