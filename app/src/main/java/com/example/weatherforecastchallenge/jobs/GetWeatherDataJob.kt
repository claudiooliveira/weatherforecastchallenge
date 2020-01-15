package com.example.weatherforecastchallenge.jobs

import com.birbit.android.jobqueue.Job
import com.birbit.android.jobqueue.Params
import com.birbit.android.jobqueue.RetryConstraint
import com.example.weatherforecastchallenge.weather.WeatherForecastRequest
import com.example.weatherforecastchallenge.weather.GetWeatherDataEvent
import com.example.weatherforecastchallenge.weather.WeatherWorker
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

class GetWeatherDataJob : Job {

    var weatherForecastRequest : WeatherForecastRequest

    constructor(weatherForecastRequest: WeatherForecastRequest) : super(Params(10).requireNetwork().groupBy("get_location")) {
        this.weatherForecastRequest = weatherForecastRequest
    }

    override fun onAdded() {

    }

    @Throws(Exception::class)
    override fun onRun() {

        val weatherWorker = WeatherWorker()

        // Getting the weather data we call the GetWeatherDataEvent event, where the "listener" will update the view.
        var weatherData = weatherWorker!!.getWeatherData(applicationContext, weatherForecastRequest.latitude, weatherForecastRequest.longitude)

        EventBus.getDefault().post(GetWeatherDataEvent(weatherData))

    }

    override fun onCancel(cancelReason: Int, throwable: Throwable?) {

    }

    override fun shouldReRunOnThrowable(throwable: Throwable, runCount: Int, maxRunCount: Int): RetryConstraint {
        return RetryConstraint.createExponentialBackoff(runCount, 2000)
    }

}