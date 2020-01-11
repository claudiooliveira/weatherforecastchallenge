package com.example.weatherforecastchallenge.api

import com.example.weatherforecastchallenge.WeatherModel
import io.reactivex.Observable
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
        @Query("exclude") exclude : String
    ) : Single<WeatherModel>

}