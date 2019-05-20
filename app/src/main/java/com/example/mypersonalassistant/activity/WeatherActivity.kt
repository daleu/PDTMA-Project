package com.example.mypersonalassistant.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.adapter.WeatherRecyclerViewAdapter
import com.example.mypersonalassistant.async.CurrentWeatherAsyncTask
import com.example.mypersonalassistant.async.WeatherAsyncTask
import com.example.mypersonalassistant.model.WeatherModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_weather.*
import java.util.*
import android.widget.LinearLayout
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.Menu


class WeatherActivity : AppCompatActivity() {

    //CURRENT WEATHER CARD
    lateinit var cv: CardView
    lateinit var cardTitleViewWeather: TextView
    lateinit var cardImageViewTitleWeather: ImageView
    lateinit var tempratureText: TextView
    lateinit var tempratureImage: ImageView
    lateinit var humidityText: TextView
    lateinit var humidityImage: ImageView
    lateinit var windText: TextView
    lateinit var windImage: ImageView
    lateinit var currentWeatherMainImage: ImageView
    lateinit var weatherConditionMain: TextView
    lateinit var weatherDate: TextView
    lateinit var myCurrentWeatherTask: CurrentWeatherAsyncTask

    var fabExpanded = false
    var fabSettings: FloatingActionButton? = null
    var layoutFabChange: LinearLayout? = null
    var layoutFabAdd: LinearLayout? = null

    //RECYCLE VIEW
    private var recyclerView: RecyclerView? = null
    var result: ArrayList<WeatherModel> = ArrayList<WeatherModel>()
    lateinit var adapter: WeatherRecyclerViewAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var myTask: WeatherAsyncTask

    //LOCATION
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    lateinit var mLastLocation: Location
    lateinit var mLastLocationTime: Date
    private val REQUEST_PERMISSION_LOCATION = 10
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        setSupportActionBar(toolbar)

        //CURRENT WEATHER
        cv = findViewById(R.id.currentWeatherCard)

        cardTitleViewWeather = findViewById(R.id.weatherHeaderMainCardtextView)
        cardImageViewTitleWeather = findViewById(R.id.weatherHeaderMainCardImageView)

        tempratureText = findViewById(R.id.tempratureMainText)
        tempratureImage = findViewById(R.id.tempratureMainImage)

        humidityText = findViewById(R.id.humidityMainText)
        humidityImage = findViewById(R.id.humidityMainImage)

        windText = findViewById(R.id.windMainText)
        windImage = findViewById(R.id.windMainImage)

        currentWeatherMainImage = findViewById(R.id.currentWeatherMainImage)
        weatherConditionMain = findViewById(R.id.weatherConditionMain)

        weatherDate = findViewById(R.id.weatherDate)

        //RECYCLE VIEW
        recyclerView = findViewById(R.id.weather_recycleview)
        adapter = WeatherRecyclerViewAdapter(result)
        layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()

        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fabSettings = this.findViewById(R.id.fabSettings) as FloatingActionButton
        layoutFabChange = this.findViewById(R.id.layoutFabChange)
        layoutFabAdd = this.findViewById(R.id.layoutFabAdd)

        fabSettings!!.setOnClickListener {
            if (fabExpanded === true) {
                closeSubMenusFab()
            } else {
                openSubMenusFab()
            }
        }
        closeSubMenusFab()
        startLocationUpdates()
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            if(!::mLastLocationTime.isInitialized) {
                onLocationChanged(locationResult.lastLocation)
            }
            else {
                val diff = Calendar.getInstance().time.getTime() - mLastLocationTime.getTime()
                val diffMinutes = diff / 60000
                if(diffMinutes>30){
                    onLocationChanged(locationResult.lastLocation)
                }
            }
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        mLastLocationTime = Calendar.getInstance().time

        myTask = WeatherAsyncTask(adapter, location)
        myTask.execute()

        myCurrentWeatherTask = CurrentWeatherAsyncTask(
                cv,
                cardTitleViewWeather,
                cardImageViewTitleWeather,
                tempratureText,tempratureImage,
                humidityText,
                humidityImage,
                windText,
                windImage,
                currentWeatherMainImage,
                weatherConditionMain,
                weatherDate,
                location)
        myCurrentWeatherTask.execute()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun closeSubMenusFab() {
        layoutFabChange?.visibility = View.INVISIBLE
        layoutFabAdd?.visibility = View.INVISIBLE
        fabSettings?.setImageResource(R.drawable.ic_settings_work_tool)
        fabExpanded = false
    }

    //Opens FAB submenus
    private fun openSubMenusFab() {
        layoutFabAdd?.visibility = View.VISIBLE
        layoutFabChange?.visibility = View.VISIBLE
        //Change settings icon to 'X' icon
        fabSettings?.setImageResource(R.drawable.ic_cancel_music)
        fabExpanded = true
    }

}
