package com.example.weatherforecastchallenge.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.weatherforecastchallenge.R

inline fun AppCompatActivity.transact(action: FragmentTransaction.() -> Unit) {
    supportFragmentManager.beginTransaction().apply {
        action()
    }.commit()
}

inline fun getWeatherIconSolid(context : Context, _icon : String) : Drawable? {
    var icon = _icon
    if (icon == "cloudy" || icon == "fog") { icon = "${icon}-${DateUtils.checkIfDayOrNight()}" }
    when(icon) {
        "clear-day" -> return ContextCompat.getDrawable(context, R.drawable.clear_day_solid)
        "clear-night" -> return ContextCompat.getDrawable(context, R.drawable.clear_night_solid)
        "cloudy-day" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_day_solid)
        "cloudy-night" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_night_solid)
        "partly-cloudy-day" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_day_solid)
        "partly-cloudy-night" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_night_solid)
        "rain" -> return ContextCompat.getDrawable(context, R.drawable.rain_solid)
        "snow" -> return ContextCompat.getDrawable(context, R.drawable.snow_solid)
        "sleet" -> return ContextCompat.getDrawable(context, R.drawable.rain_solid)
        "wind" -> return ContextCompat.getDrawable(context, R.drawable.wind_solid)
        "fog-day" -> return ContextCompat.getDrawable(context, R.drawable.fog_day_solid)
        "fog-night" -> return ContextCompat.getDrawable(context, R.drawable.fog_night_solid)
        else -> {
            return ContextCompat.getDrawable(context, R.drawable.clear_day_solid)
        }
    }
}

inline fun getWeatherIconFlat(context : Context, _icon : String) : Drawable? {
    var icon = _icon
    if (icon == "cloudy" || icon == "fog") { icon = "${icon}-${DateUtils.checkIfDayOrNight()}" }
    when(icon) {
        "clear-day" -> return ContextCompat.getDrawable(context, R.drawable.clear_day_flat)
        "clear-night" -> return ContextCompat.getDrawable(context, R.drawable.clear_night_flat)
        "cloudy-day" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_day_flat)
        "cloudy-night" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_night_flat)
        "partly-cloudy-day" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_day_flat)
        "partly-cloudy-night" -> return ContextCompat.getDrawable(context, R.drawable.cloudy_night_flat)
        "rain" -> return ContextCompat.getDrawable(context, R.drawable.rain_flat)
        "snow" -> return ContextCompat.getDrawable(context, R.drawable.snow_flat)
        "sleet" -> return ContextCompat.getDrawable(context, R.drawable.rain_flat)
        "wind" -> return ContextCompat.getDrawable(context, R.drawable.wind_flat)
        "fog-day" -> return ContextCompat.getDrawable(context, R.drawable.fog_day_flat)
        "fog-night" -> return ContextCompat.getDrawable(context, R.drawable.fog_night_flat)
        else -> {
            return ContextCompat.getDrawable(context, R.drawable.clear_day_flat)
        }
    }
}