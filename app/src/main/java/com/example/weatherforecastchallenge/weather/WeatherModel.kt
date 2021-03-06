package com.example.weatherforecastchallenge.weather

import com.google.gson.annotations.Expose

class WeatherModel {

    @Expose open var latitude : Double? = null
    @Expose open var longitude : Double? = null
    @Expose open var timezone : String? = null
    @Expose open var offset : Int? = null
    @Expose open var currently : WeatherDataModel? = null
    @Expose open var daily : WeatherCollectionDataModel? = null

}

data class WeatherCollectionDataModel (
    @Expose var summary : String,
    @Expose var icon : String,
    @Expose var data : List<WeatherDataModel>
)

data class WeatherDataModel (
    @Expose var time : Long,
    @Expose var summary : String,
    @Expose var icon : String,
    @Expose var temperature : Float,
    @Expose var apparentTemperature : Float,
    @Expose var temperatureHigh : Float,
    @Expose var temperatureLow : Float,
    @Expose var humidity : Float
)