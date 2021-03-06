package com.example.weatherforecastchallenge.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.weatherforecastchallenge.BaseApp
import com.example.weatherforecastchallenge.Consts

import com.example.weatherforecastchallenge.R
import com.example.weatherforecastchallenge.utils.DateUtils
import com.example.weatherforecastchallenge.utils.LoadingDialog
import com.example.weatherforecastchallenge.utils.getWeatherIconFlat
import com.example.weatherforecastchallenge.utils.getWeatherIconSolid
import com.example.weatherforecastchallenge.weather.GetWeatherDataEvent
import com.example.weatherforecastchallenge.weather.WeatherDataModel
import com.example.weatherforecastchallenge.weather.WeatherForecastRequest
import com.example.weatherforecastchallenge.weather.WeatherModel
import com.example.weatherforecastchallenge.jobs.GetWeatherDataJob
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.forecast_day_item.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
    lateinit var txtAddress : TextView
    lateinit var txtTitle : TextView
    lateinit var txtApparentTemperature : TextView
    lateinit var rvNextDays : RecyclerView
    lateinit var imgWeatherIcon : ImageView
    lateinit var currentWeatherLayout : LinearLayout
    lateinit var homeContraintLayout : ConstraintLayout
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var state = ""
    var city = ""
    var isLoading = false
    lateinit var dialog : LoadingDialog
    lateinit var fadeFromTopToBottomAnim : Animation
    lateinit var zoomOutAnim : Animation
    lateinit var fadeFromLeftToRightSlowAnim : Animation
    lateinit var bounceRotateFromTopAnim : Animation

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        dialog = LoadingDialog(activity!!)

        //Load all animations
        loadAnimations()

        //Configure fragment to connect with interactor
        HomeConfigurator.configureFragment(this)

        //Bind views...
        bindViews(view)

        //Request user location
        requestUserLocation()

        return view

    }

    fun loadAnimations() {
        fadeFromTopToBottomAnim = AnimationUtils.loadAnimation(context!!, R.anim.fade_from_top_to_bottom)
        zoomOutAnim = AnimationUtils.loadAnimation(context!!, R.anim.zoom_out)
        fadeFromLeftToRightSlowAnim = AnimationUtils.loadAnimation(context!!, R.anim.fade_from_left_to_right_slow)
        bounceRotateFromTopAnim = AnimationUtils.loadAnimation(context!!, R.anim.bounce_rotate_from_top)
    }

    @SuppressLint("MissingPermission")
    fun requestUserLocation() {
        showLoading()
        if (checkPermissions()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                latitude = location!!.latitude
                longitude = location!!.longitude
                val geocoder = Geocoder(context!!, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                state = addresses[0].adminArea
                city = addresses[0].subAdminArea
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
        homeContraintLayout = view.findViewById(R.id.homeContraintLayout)
        txtDegrees = view.findViewById(R.id.txtDegrees)
        txtAddress = view.findViewById(R.id.txtAddress)
        txtTitle = view.findViewById(R.id.txtTitle)
        currentWeatherLayout = view.findViewById(R.id.currentWeatherLayout)
        txtApparentTemperature = view.findViewById(R.id.txtApparentTemperature)
        imgWeatherIcon = view.findViewById(R.id.imgWeatherIcon)
        rvNextDays = view.findViewById(R.id.rvNextDays)
    }

    fun fetchData() {
        val request = WeatherForecastRequest(latitude, longitude)
        BaseApp.getCurrentJobManager()!!.addJobInBackground(
            GetWeatherDataJob(
                request
            )
        )
        showLoading()
    }

    override fun displayCurrentAddress() {
        txtAddress.text = "${city}, ${state}"
        txtAddress.animation = fadeFromTopToBottomAnim
    }

    override fun displayWeatherData(weatherData : WeatherModel) {

        this.weatherData = weatherData

        displayCurrentDate()

        //Display current user address based on location
        displayCurrentAddress()

        displayDegrees()

        displayApparentTemperature()

        displayWeatherIcon()

        displayForecastNextDays()

    }

    override fun displayCurrentDate() {
        var weekday = DateUtils.getDateFromFormat(Date(), "EEEE").capitalize()
        var currentDate = DateUtils.getCurrentDateFromFormat("DD '${resources.getString(R.string.of)}' MMM")
        txtTitle.text = "${weekday}, ${currentDate}"
        txtTitle.animation = fadeFromTopToBottomAnim
    }

    override fun displayDegrees() {
        txtDegrees.text = Math.round(weatherData.currently!!.temperature).toString()
        currentWeatherLayout.animation = zoomOutAnim
    }

    override fun displayApparentTemperature() {
        txtApparentTemperature.text = "${weatherData.currently!!.summary}\n${resources.getString(R.string.apparent_temperature)}: ${Math.round(weatherData.currently!!.apparentTemperature)}ºC"
        txtApparentTemperature.animation = fadeFromTopToBottomAnim
    }

    override fun displayWeatherIcon() {
        imgWeatherIcon.setImageDrawable(getWeatherIconFlat(context!!, weatherData.currently!!.icon))
        imgWeatherIcon.animation = bounceRotateFromTopAnim
    }

    override fun displayForecastNextDays() {
        rvNextDays.setHasFixedSize(true)
        var forecastNextDays = weatherData.daily!!.data
        rvNextDays.adapter = ForecastNextDaysAdapter(forecastNextDays.drop(1), context!!) //Since the challenge requires displaying the forecast next 7 days, I used list.drop (1) to remove the first item, because the API returns the current day in this array as well.
        rvNextDays.layoutManager = LinearLayoutManager(context!!, LinearLayout.VERTICAL, false)

    }

    override fun showLoading() {
        loading(true)
        homeContraintLayout.visibility = View.INVISIBLE
    }

    override fun hideLoading() {
        loading(false)
        homeContraintLayout.visibility = View.VISIBLE
    }

    fun loading(show : Boolean) {
        if (show && !isLoading) {
            isLoading = true
            dialog.showDialog()
        }else if (!show && isLoading) {
            isLoading = false
            dialog.hideDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event : GetWeatherDataEvent) {

        hideLoading()
        displayWeatherData(event.weatherData)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == Consts.GEOLOCATION_PERMISSIONS_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                requestUserLocation()
            }else{
                fetchData()
            }
        }else{
            fetchData()
        }
    }

    /*
    * The adapter required to display the forecast for the coming days.
    * I decided to create it in here because we will not use it in another situation.
    **/

    class ForecastNextDaysAdapter(val nextDaysData : List<WeatherDataModel>, val context : Context) : RecyclerView.Adapter<ForecastNextDaysAdapter.ViewHolder>(), View.OnClickListener {

        var isViewExpanded = false
        val slideFromTopToBottomAnim = AnimationUtils.loadAnimation(context!!, R.anim.slide_from_top_to_bottom)
        val slideFromBottomToTopAnim = AnimationUtils.loadAnimation(context!!, R.anim.slide_from_bottom_to_top)
        lateinit var view : View

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            view = LayoutInflater.from(context).inflate(R.layout.forecast_day_item, parent, false)
            view.setOnClickListener(this)
            if (!isViewExpanded) {
                view.expandedLayout.visibility = View.GONE
                view.expandedLayout.isEnabled = false
            }
            return ViewHolder(view)
        }

        override fun onClick(view: View?) {

            if (view!!.expandedLayout.visibility == View.GONE) {
                view!!.expandedLayout.animation = slideFromTopToBottomAnim
                isViewExpanded = true
                view!!.expandedLayout.visibility = View.VISIBLE
                view!!.expandedLayout.isEnabled = true
                slideFromTopToBottomAnim.start()
            }else{

                slideFromBottomToTopAnim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        view!!.expandedLayout.visibility = View.GONE
                        view!!.expandedLayout.isEnabled = false
                        isViewExpanded = false
                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }
                })

                view!!.expandedLayout.animation = slideFromBottomToTopAnim
                isViewExpanded = false
                slideFromBottomToTopAnim.start()

            }
        }

        override fun getItemCount(): Int {
            return nextDaysData.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val weatherData = nextDaysData[position]
            holder?.let {
                it.bindView(context, weatherData, position + 1)
            }
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindView(context : Context, weatherData: WeatherDataModel, position : Int) {
                val fadeFromLeftToRightSlowAnim = AnimationUtils.loadAnimation(context!!, R.anim.fade_from_left_to_right_slow)
                var weekday = DateUtils.getDateFromFormat(DateUtils.unixTimestampToDate(weatherData.time), "EE").capitalize()
                var date = DateUtils.getDateFromFormat(DateUtils.unixTimestampToDate(weatherData.time), "DD/MM")
                itemView.txtTemperature.text = "${date} - ${weekday}. -  Min. ${Math.round(weatherData.temperatureLow)}ºC - Máx. ${Math.round(weatherData.temperatureHigh)}ºC"
                itemView.imgWeatherIcon.setImageDrawable(getWeatherIconSolid(context!!, weatherData.icon))
                fadeFromLeftToRightSlowAnim.duration = (300 * position).toLong()
                itemView.containerLayout.animation = fadeFromLeftToRightSlowAnim
                itemView.txtSummary.text = "${weatherData.summary}"
            }
        }

    }


}
