package com.example.weatherforecastchallenge.utils

import android.content.Context
import android.util.Log
import com.example.weatherforecastchallenge.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateUtils {
    val TAG = DateUtils::class.java.simpleName
    val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    fun getUTCStringAsDate(datetime: String): Date? {
        var date: Date? = null
        val sf = SimpleDateFormat(ISO_FORMAT)
        sf.timeZone = TimeZone.getTimeZone("UTC")
        try {
            date = sf.parse(datetime)
        } catch (e: ParseException) {
            Log.e(TAG, "Error converting datetime")
            e.printStackTrace()
        }

        return date
    }

    fun getDateAsUTCString(date: Date): String {
        val df = SimpleDateFormat(ISO_FORMAT)
        df.timeZone = TimeZone.getTimeZone("UTC")
        return df.format(date)
    }

    fun unixTimestampToDate(timestamp : Long) : Date {
        return Date(timestamp*1000)
    }

    fun getCurrentDateFromFormat(format : String) : String {
        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        var newDate: Date? = null
        try {
            newDate = f.parse(f.format(Date()))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (newDate != null) {
            f.applyPattern(format)
            return f.format(newDate)
        } else {
            return "Invalid date"
        }
    }

    fun getDateFromFormat(date: Date, format: String): String {
        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        var newDate: Date? = null
        try {
            newDate = f.parse(f.format(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (newDate != null) {
            f.applyPattern(format)
            return f.format(newDate)
        } else {
            return "Invalid date"
        }
    }

    fun checkIfDayOrNight(): String {
        val f = SimpleDateFormat("HH")
        var newDate: Date? = null
        try {
            newDate = f.parse(f.format(Date()))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (newDate != null) {
            var time = f.format(newDate).toInt()
            if (time in 5..19) {
                return "day"
            }else{
                return "night"
            }
        } else {
            return "Invalid date"
        }
    }

    fun parse(date : String) : Date? {
        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        var newDate: Date? = null
        try {
            newDate = f.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDate
    }
}