package com.example.weatherforecastchallenge.home

import com.example.weatherforecastchallenge.weather.WeatherModel
import java.lang.ref.WeakReference

interface HomePresenterInput {
    fun presentWeatherData(weatherData: WeatherModel)
}

class HomePresenter : HomePresenterInput {

    var output: WeakReference<HomeFragmentInput>? = null

    override fun presentWeatherData(weatherData: WeatherModel) {

        output?.get()?.displayWeatherData(weatherData)

    }

}