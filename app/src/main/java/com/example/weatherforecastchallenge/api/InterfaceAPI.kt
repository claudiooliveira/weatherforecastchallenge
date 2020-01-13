package com.example.weatherforecastchallenge.api

import com.example.weatherforecastchallenge.weather.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InterfaceAPI {

    @GET("{lat},{lng}")
    fun getWeatherData(
        @Path("lat") lat : Double,
        @Path("lng") lng : Double,
        @Query("lang") lang : String,
        @Query("exclude") exclude : String,
        @Query("units") units : String = "si"
    ) : Single<WeatherModel>

}