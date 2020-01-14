package com.example.weatherforecastchallenge.home

import android.content.Context
import android.os.StrictMode
import com.example.weatherforecastchallenge.api.APICore
import com.example.weatherforecastchallenge.weather.WeatherWorker


interface HomeInteractorInput {
    fun fetchWeatherData(context : Context, request: WeatherForecastRequest)
}

class HomeInteractor : HomeInteractorInput {

    var output: HomePresenterInput? = null
    val weatherWorker = WeatherWorker()

    override fun fetchWeatherData(context : Context, request: WeatherForecastRequest) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        var weatherData = weatherWorker!!.getWeatherData(context, request.latitude, request.longitude)
        output?.presentWeatherData(weatherData)

    }
}