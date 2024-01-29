package com.jpmorgan.test.api

import com.jpmorgan.test.utils.APIConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(APIConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    object ApiClient {
        val apiService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}