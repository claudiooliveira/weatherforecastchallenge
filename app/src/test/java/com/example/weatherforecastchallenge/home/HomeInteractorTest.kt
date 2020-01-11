package com.example.weatherforecastchallenge.home

import androidx.test.core.app.ApplicationProvider
import com.example.weatherforecastchallenge.weather.WeatherModel
import org.junit.Assert
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomeInteractorTest {

    @Test
    fun fetchWeatherData_with_validInput_shouldCall_presentWeatherData() {

        val homeInteractor = HomeInteractor()
        val request = WeatherForecastRequest(-24.0368638,-46.5370516)
        val homePresenterInputSpy = HomePresenterInputSpy()

        homeInteractor.output = homePresenterInputSpy
        homeInteractor.fetchWeatherData(ApplicationProvider.getApplicationContext(), request)

        // Then
        Assert.assertTrue("When the valid input is passed to HomeInteractor "
                + "Then presentWeatherData should be called",
            homePresenterInputSpy.presentWeatherDataIsCalled)

    }

    private inner class HomePresenterInputSpy: HomePresenterInput {

        internal var presentWeatherDataIsCalled = false
        internal var weatherDataCopy: WeatherModel? = null
        override fun presentWeatherData(weatherData: WeatherModel) {
            presentWeatherDataIsCalled = true
            weatherDataCopy = weatherData
        }
    }
}