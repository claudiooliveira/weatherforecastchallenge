package com.example.weatherforecastchallenge.weather

import android.content.Context
import com.example.weatherforecastchallenge.api.APICore

interface WeatherWorkerInput {
    fun getWeatherData(context : Context, latitude : Double, longitude : Double) : WeatherModel
}

class WeatherWorker : WeatherWorkerInput {
    override fun getWeatherData(context : Context, latitude : Double, longitude : Double) : WeatherModel {
        return APICore().getAPI(context).getWeatherData(
            latitude,
            longitude,
            "pt",
            "hourly,flags")
            .blockingGet()
    }
}