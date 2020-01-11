package com.example.weatherforecastchallenge.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.weatherforecastchallenge.R
import com.example.weatherforecastchallenge.weather.WeatherModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * Created by Claudio Oliveira 10/01/2020
 * Home Fragment
 */

interface HomeFragmentInput {
    fun displayWeatherData(weatherData : WeatherModel)
}

class HomeFragment : Fragment(), HomeFragmentInput {

    lateinit var output: HomeInteractorInput

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        HomeConfigurator.configureFragment(this)
        fetchData()

        return view
    }

    fun fetchData() {
        // create Request and set the needed input
        val request = WeatherForecastRequest(-24.0368638,-46.5370516)
        // Call the output to fetch the data
        output.fetchWeatherData(context!!, request)
    }

    override fun displayWeatherData(weatherData : WeatherModel) {
        Log.v("testando!!!!", weatherData.daily!!.summary)
    }

}
