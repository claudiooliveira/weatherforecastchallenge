package com.example.weatherforecastchallenge.home


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.example.weatherforecastchallenge.R
import com.example.weatherforecastchallenge.utils.DateUtils
import com.example.weatherforecastchallenge.weather.WeatherDataModel
import com.example.weatherforecastchallenge.weather.WeatherModel
import kotlinx.android.synthetic.main.forecast_day_item.view.*
import java.util.*

/**
 * Created by Claudio Oliveira 10/01/2020
 * Home Fragment
 */

interface HomeFragmentInput {
    fun displayWeatherData(weatherData : WeatherModel)
}

class HomeFragment : Fragment(), HomeFragmentInput, HomeView {

    lateinit var output: HomeInteractorInput
    lateinit var weatherData : WeatherModel
    lateinit var txtDegrees : TextView
    lateinit var txtTitle : TextView
    lateinit var rvNextDays : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Configure fragment to connect with interactor
        HomeConfigurator.configureFragment(this)

        //Bind views...
        bindViews(view)

        //Fetch weather data
        fetchData()

        return view
    }

    fun bindViews(view : View) {
        txtDegrees = view.findViewById(R.id.txtDegrees)
        txtTitle = view.findViewById(R.id.txtTitle)
        rvNextDays = view.findViewById(R.id.rvNextDays)
    }

    fun fetchData() {
        val request = WeatherForecastRequest(-24.0368638, -46.5370516)
        output.fetchWeatherData(context!!, request)
    }

    override fun displayWeatherData(weatherData : WeatherModel) {
        this.weatherData = weatherData

        //Display current date
        displayCurrentDate()

        //Display current temperature
        displayDegrees()

        //Display forecast for next days
        displayForecastNextDays()

    }

    override fun displayCurrentDate() {
        var weekday = DateUtils.getDateFromFormat(Date(), "EEEE").capitalize()
        var currentDate = DateUtils.getCurrentDateFromFormat("DD '${resources.getString(R.string.of)}' MMM")
        txtTitle.text = "${weekday}, ${currentDate}"
    }

    override fun displayDegrees() {
        txtDegrees.text = Math.round(weatherData.currently!!.temperature).toString()
    }

    override fun displayForecastNextDays() {
        rvNextDays.setHasFixedSize(true)
        var forecastNextDays = weatherData.daily!!.data
        rvNextDays.adapter = ForecastNextDaysAdapter(forecastNextDays.drop(1), context!!) //Since the challenge requires displaying the forecast next 7 days, I used list.drop (1) to remove the first item, because the API returns the current day in this array as well.
        rvNextDays.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)
    }

    /*
    * The adapter required to display the forecast for the coming days.
    * I decided to create it in here because we will not use it in another situation.
    **/

    class ForecastNextDaysAdapter(val nextDaysData : List<WeatherDataModel>, val context : Context) : RecyclerView.Adapter<ForecastNextDaysAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.forecast_day_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return nextDaysData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val weatherData = nextDaysData[position]
            holder?.let {
                it.bindView(weatherData)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(weatherData: WeatherDataModel) {
                var weekday = DateUtils.getDateFromFormat(DateUtils.unixTimestampToDate(weatherData.time), "EEEE").capitalize()
                var date = DateUtils.getDateFromFormat(DateUtils.unixTimestampToDate(weatherData.time), "DD/MM");
                itemView.txtTemperature.text = "${date} - ${weekday} -  Min. ${Math.round(weatherData.temperatureLow)}ºC - Máx. ${Math.round(weatherData.temperatureHigh)}ºC"
            }
        }

    }


}
