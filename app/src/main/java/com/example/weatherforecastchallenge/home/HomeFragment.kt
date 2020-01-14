package com.example.weatherforecastchallenge.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.weatherforecastchallenge.Consts

import com.example.weatherforecastchallenge.R
import com.example.weatherforecastchallenge.utils.DateUtils
import com.example.weatherforecastchallenge.weather.WeatherDataModel
import com.example.weatherforecastchallenge.weather.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.forecast_day_item.view.*
import java.lang.Exception
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
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //Configure fragment to connect with interactor
        HomeConfigurator.configureFragment(this)

        //Bind views...
        bindViews(view)

        //Request user location
        requestUserLocation()

        //Fetch weather data
        fetchData()

        return view
    }

    @SuppressLint("MissingPermission")
    fun requestUserLocation() {
        if (checkPermissions()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                latitude = location!!.latitude
                longitude = location!!.longitude
                fetchData()
            }
        }else{
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            Consts.GEOLOCATION_PERMISSIONS_ID
        )
    }

    fun bindViews(view : View) {
        txtDegrees = view.findViewById(R.id.txtDegrees)
        txtTitle = view.findViewById(R.id.txtTitle)
        rvNextDays = view.findViewById(R.id.rvNextDays)
    }

    fun fetchData() {
        val request = WeatherForecastRequest(latitude, longitude)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == Consts.GEOLOCATION_PERMISSIONS_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestUserLocation()
            }
        }
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
