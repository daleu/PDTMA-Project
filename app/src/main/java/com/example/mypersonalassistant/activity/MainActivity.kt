package com.example.mypersonalassistant.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.mypersonalassistant.BuildConfig
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.adapter.MainRecyclerLastQueryViewAdapter
import com.example.mypersonalassistant.service.OpenWeatherMapService
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.async.MainAsyncTask
import com.example.mypersonalassistant.helper.SpeechRecognizerHelper
import com.example.mypersonalassistant.model.MainAdapterModel
import com.example.mypersonalassistant.model.QueryModel
import com.example.mypersonalassistant.model.WeatherModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.IOException
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //RECYCLE VIEW DETAIL
    private var recyclerView: RecyclerView? = null
    var result: ArrayList<MainAdapterModel> = ArrayList<MainAdapterModel>()
    lateinit var adapter: MainRecyclerViewAdapter
    lateinit var layoutManagerLastQuery: LinearLayoutManager

    //RECYCLE VIEW QUERIES
    private var recyclerViewLastQuery: RecyclerView? = null
    var resultLastQuery: ArrayList<QueryModel> = ArrayList<QueryModel>()
    lateinit var adapterLastQuery: MainRecyclerLastQueryViewAdapter
    lateinit var myTask: MainAsyncTask
    lateinit var layoutManager: LinearLayoutManager

    //LOCATION
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    lateinit var mLastLocation: Location
    lateinit var mLastLocationTime: Date
    private val REQUEST_PERMISSION_LOCATION = 10
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000

    //SPEECH CODE
    private var SPEECH_REQUEST_CODE: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //RECYCLE VIEW
        recyclerView = findViewById(R.id.main_recycleview)
        adapter = MainRecyclerViewAdapter(result, this)
        layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.layoutManager?.scrollToPosition(Integer.MAX_VALUE / 2)

        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

        //RECYCLE VIEW LAST QUERy
        recyclerViewLastQuery = findViewById(R.id.main_lastquery_recycleview)
        adapterLastQuery = MainRecyclerLastQueryViewAdapter(generateDataLastQuery())
        layoutManagerLastQuery = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerViewLastQuery?.layoutManager = layoutManagerLastQuery
        recyclerViewLastQuery?.itemAnimator = DefaultItemAnimator()

        recyclerViewLastQuery?.adapter = adapterLastQuery
        adapterLastQuery.notifyDataSetChanged()

        //FLOATING BUTTON
        fab.setOnClickListener { view ->
            displaySpeechRecognizer()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.itemIconTintList = null

        Log.i("Location","Before Request")
        mLocationRequest = LocationRequest()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        startLocationUpdates()

    }

    fun generateDataLastQuery(): ArrayList<QueryModel> {
        var queries = ArrayList<QueryModel>()
        var query = QueryModel(1,"Check Weather","bla bla","weather")
        var query2 = QueryModel(1,"Check Calendar","bla bla","calendar")
        var query3 = QueryModel(1,"Check To Do List","bla bla","todo")
        queries.add(query)
        queries.add(query2)
        queries.add(query3)

        return queries
    }

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

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

        val geocoder: Geocoder = Geocoder(this, Locale.ENGLISH)
        val prefGen = getSharedPreferences("General", ContextWrapper.MODE_PRIVATE).edit()
        try {
            var address = geocoder.getFromLocation(location.latitude,location.longitude,1)
            if(address!=null){
                var locality = address.get(0).locality
                prefGen.putString("currentLocality",locality).apply()
            }
            else {
                prefGen.putString("currentLocality","Not Found").apply()
            }
        } catch(error: IOException){
            prefGen.putString("currentLocality","Not Found").apply()
        }

        myTask = MainAsyncTask(adapter, layoutManager, location, this)
        myTask.execute()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_weather -> {
                val intent = Intent(this, WeatherActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_calendar -> {
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_to_do -> {
                val intent = Intent(this, ToDoActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySpeechRecognizer(){
        var intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var results: List<String> = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var spokenText = results.get(0)
            val speechRrecognizerHelper = SpeechRecognizerHelper(this)
            speechRrecognizerHelper.speechQuery(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if(::mLastLocation.isInitialized){
            myTask = MainAsyncTask(adapter, layoutManager, mLastLocation, this)
            myTask.execute()
        }
    }
}
