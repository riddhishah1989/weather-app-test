package com.jpmorgan.test

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherAppUITest {

    @Before
    fun setUp() {
        // You can perform any setup here, such as launching the app or initializing test data.
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
