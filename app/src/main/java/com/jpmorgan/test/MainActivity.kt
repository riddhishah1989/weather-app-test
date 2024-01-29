package com.jpmorgan.test

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jpmorgan.test.data.WeatherModel
import com.jpmorgan.test.databinding.ActivityMainBinding
import com.jpmorgan.test.utils.APIConstants
import com.jpmorgan.test.utils.CommonUtils
import com.jpmorgan.test.utils.Constants
import com.jpmorgan.test.utils.SharedPrefs
import com.jpmorgan.test.utils.location_helper.LocationHelper
import com.jpmorgan.test.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity(), LocationHelper.LocationUpdateListener {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherViewModel = ViewModelProvider(this@MainActivity)[WeatherViewModel::class.java]

        weatherViewModel.weatherData.observe(this, Observer { weatherData ->
            updateUI(weatherData)
        })

        //init LocationHelper class to get the current Location
        initLocationService()

        binding.btnSearch.setOnClickListener {
            if (binding.atvSearchEdit.text.isNotEmpty()) {
                binding.llResponse.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE

                //Storing city name for next time fetch
                SharedPrefs.setString(
                    Constants.SHARED_PREF_CITY_NAME_KEY,
                    binding.atvSearchEdit.text.toString()
                )

                weatherViewModel.getWeatherData(binding.atvSearchEdit.text.toString())
            } else {
                CommonUtils.showMessage(this, "Please enter a city name")
            }
        }
    }

    /*
    * Update UI after getting successful response
    */
    private fun updateUI(weatherData: WeatherModel) {
        binding.tvCityName.text = "City is ${weatherData.name}"
        binding.tvTemprature.text =
            "Current temprature is ${weatherData.main?.temp.toString()} F and feels like ${weatherData.main?.feelsLike} F"
        binding.tvDescription.text = "Sky condition is ${weatherData.weather.get(0).description} today"


        binding.llResponse.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        // Load weather icon using Glide
        val imageUrl: String =
            APIConstants.WEATHER_ICON_URL + weatherData.weather[0].icon + APIConstants.WEATHER_ICON_SIZE_EXTENSION
        CommonUtils.showImage(this, imageUrl, binding.ivWeatherImage)
    }

    /*
    * Initialise the Location Service to get the current location
    */
    private fun initLocationService() {
        locationHelper = LocationHelper(this, this)
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
    }

    override fun onStop() {
        super.onStop()
        locationHelper.stopLocationUpdates()
    }

    override fun onLocationUpdate(location: Location) {
        var cityName = SharedPrefs.getString(Constants.SHARED_PREF_CITY_NAME_KEY)
        if (cityName.isEmpty()) {
            val latitude = location.latitude
            val longitude = location.longitude
            cityName = CommonUtils.getCityNameFromLatLong(this@MainActivity, latitude, longitude)
        }
        weatherViewModel.getWeatherData(cityName)
    }

    private fun checkLocationPermission() {
        if (locationHelper.isLocationPermissionGranted()) {
            locationHelper.requestLocationUpdates()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationHelper.requestLocationUpdates()
            } else {
                // Handle permission denied
                CommonUtils.showMessage(
                    this,
                    "Please allow a location permission to get the weather details"
                )
            }
        }
}