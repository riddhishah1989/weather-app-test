package com.jpmorgan.test

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WeatherAppTestCases {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jpmorgan.test", appContext.packageName)
    }

    @Test
    fun searchWeatherForCity() {
        // Launch the app
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        // Wait for the app to start and load
        Thread.sleep(2000)

        // Find the search EditText and enter a city name
        Espresso.onView(ViewMatchers.withId(R.id.atvSearchEdit))
            .perform(ViewActions.typeText("New York"), ViewActions.closeSoftKeyboard())

        // Click the search button
        Espresso.onView(ViewMatchers.withId(R.id.btnSearch))
            .perform(ViewActions.click())

        // Wait for the weather data to load (you can adjust the waiting time)
        Thread.sleep(5000)

        // Verify that the weather data is displayed
        Espresso.onView(ViewMatchers.withId(R.id.tvTemprature))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}