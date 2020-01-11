package com.example.weatherforecastchallenge.home

import android.content.Context
import android.os.StrictMode
import com.example.weatherforecastchallenge.api.APICore


interface HomeInteractorInput {
    fun fetchWeatherData(context : Context, request: WeatherForecastRequest)
}

class HomeInteractor : HomeInteractorInput {

    var output: HomePresenterInput? = null

    override fun fetchWeatherData(context : Context, request: WeatherForecastRequest) {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        var weaterData = APICore().getAPI(context).getWeatherData(
            request.latitude,
            request.longitude,
            "pt",
            "hourly,flags")
            .blockingGet()

        output?.presentWeatherData(weaterData)

    }
}