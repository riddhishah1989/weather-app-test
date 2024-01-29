package com.jpmorgan.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpmorgan.test.WeatherApp
import com.jpmorgan.test.api.RetrofitClient
import com.jpmorgan.test.data.WeatherModel
import com.jpmorgan.test.utils.APIConstants
import com.jpmorgan.test.utils.CommonUtils
import kotlinx.coroutines.launch


class WeatherViewModel() : ViewModel() {

    private val apiService = RetrofitClient.ApiClient.apiService

    private val _weatherData = MutableLiveData<WeatherModel>()
    val weatherData: LiveData<WeatherModel>
        get() = _weatherData


    fun getWeatherData(cityName: String) {
        val context = WeatherApp.appContext
        if (!CommonUtils.isInternetConnected(context)) {
            CommonUtils.showMessage(context, "No Internet Connection found")
            return
        }
        viewModelScope.launch {
            try {
                val response = apiService.getWeatherData(
                    cityName,
                    APIConstants.WEATHER_API_KEY,
                    APIConstants.TEMPERATURE_UNIT
                )
                if (response.isSuccessful) {
                    println("Response: ${response.toString()}")
                    _weatherData.postValue(response.body())
                } else {
                    // Handle error
                    CommonUtils.showMessage(context, response.message())
                }
            } catch (e: Exception) {
                // Handle exception
                CommonUtils.showMessage(context, e.message.toString())
            }
        }
    }

}