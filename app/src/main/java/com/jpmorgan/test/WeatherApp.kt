package com.jpmorgan.test

import android.app.Application
import android.content.Context
import com.jpmorgan.test.utils.SharedPrefs

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this.applicationContext
        SharedPrefs.init(this)
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}