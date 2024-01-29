package com.jpmorgan.test.api

import com.jpmorgan.test.data.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather?")
    suspend fun getWeatherData(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ): Response<WeatherModel>
}