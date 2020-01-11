package com.example.weatherforecastchallenge.weather

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class WeatherWorkerTest{

    @Test
    fun getWatherData_shouldReturn_weatherData() {

        val weatherWorker = WeatherWorker()
        val weatherData = weatherWorker.getWeatherData(ApplicationProvider.getApplicationContext(), -24.0368638,-46.5370516)

        assertEquals(weatherData.timezone, "America/Sao_Paulo")
        assertNotNull(weatherData.daily)
        assertNotNull(weatherData.daily!!.data)

    }

}