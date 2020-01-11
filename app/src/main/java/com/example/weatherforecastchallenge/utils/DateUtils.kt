package com.example.weatherforecastchallenge.utils

import android.util.Log
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

    fun getMillisAsUTCString(millis: Long): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        val df = SimpleDateFormat(ISO_FORMAT)
        df.timeZone = TimeZone.getTimeZone("UTC")
        return df.format(cal.time)
    }

    fun getDate(date: String): String {
        return getDateFromFormat(date, "dd/MM/yyyy 'às' hh:mm")
    }

    fun getDateFromFormat(date: String, format: String): String {
        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        var newDate: Date? = null
        try {
            newDate = f.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (newDate != null) {
            val diff = newDate.time - Date().time
            val hours = -TimeUnit.MILLISECONDS.toHours(diff)
            val minutes = -TimeUnit.MILLISECONDS.toMinutes(diff)

            if (hours < 24) {
                return if (minutes < 60) {
                    if (minutes <= 1) {
                        "Agora pouco"
                    } else {
                        minutes.toString() + " minutos atrás"
                    }
                } else {
                    hours.toString() + "h atrás"
                }
            } else {
                f.applyPattern(format)
                return f.format(newDate)
            }
        } else {
            return date
        }
    }

    fun getDateFromFormat(newDate: Date, format: String, checkTimeIsNear : Boolean = false): String {
        val f = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        if (newDate != null) {
            val diff = newDate.time - Date().time
            val hours = -TimeUnit.MILLISECONDS.toHours(diff)
            val minutes = -TimeUnit.MILLISECONDS.toMinutes(diff)

            if (!checkTimeIsNear) {
                f.applyPattern(format)
                return f.format(newDate)
            }

            if (hours < 24) {
                return if (minutes < 60) {
                    if (minutes <= 1) {
                        "Agora pouco"
                    } else {
                        minutes.toString() + " minutos atrás"
                    }
                } else {
                    hours.toString() + "h atrás"
                }
            } else {
                f.applyPattern(format)
                return f.format(newDate)
            }
        } else {
            return newDate
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