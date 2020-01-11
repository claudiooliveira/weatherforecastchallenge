package com.example.weatherforecastchallenge.api

import android.content.Context
import android.util.Log
import com.example.weatherforecastchallenge.Consts
import com.example.weatherforecastchallenge.utils.DateUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class APICore {

    fun getAPI(ctx : Context) : InterfaceAPI {

        val prefs = ctx.getSharedPreferences(Consts.PREFS_FILENAME, 0)
        val key = prefs!!.getString(Consts.DARK_SKY_KEY, "fefe7e6b0fc373c40fadab9e2a36c87b")

        val timeout: Long = 60 * 15
        val client = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val req = chain.request()

                val request = req.newBuilder()
                    .method(req.method(), req.body())
                    .build()

                chain.proceed(request)
            }
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, typeOfT, context ->
                if (json != null && json.isJsonPrimitive) {
                    DateUtils.getUTCStringAsDate(json.asString)
                } else null
            })
            .registerTypeAdapter(Date::class.java, JsonSerializer<Date> { src, typeOfSrc, context ->
                if (src != null) {
                    JsonPrimitive(DateUtils.getDateAsUTCString(src))
                } else null
            })
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://api.darksky.net/forecast/$key/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(InterfaceAPI::class.java)

    }

}