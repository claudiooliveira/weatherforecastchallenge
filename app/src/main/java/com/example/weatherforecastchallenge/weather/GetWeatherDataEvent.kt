package com.example.weatherforecastchallenge.weather

/*
* Event called when background weather forecast is obtained
* */

class GetWeatherDataEvent {

    var weatherData : WeatherModel

    constructor(weatherData : WeatherModel) {
        this.weatherData = weatherData
    }

}