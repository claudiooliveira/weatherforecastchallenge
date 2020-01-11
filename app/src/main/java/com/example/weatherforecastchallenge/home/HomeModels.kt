package com.example.weatherforecastchallenge.home

import com.example.weatherforecastchallenge.WeatherModel
import java.util.ArrayList

//data class HomeViewModel(
//    // TODO - filter to have only the needed data
//    var listOfFlights: ArrayList<FlightViewModel>? = null
//)
data class WeatherForecastRequest(var latitude : Double, var longitude : Double)
data class HomeResponse(var listOfWeather: ArrayList<WeatherModel>? = null)