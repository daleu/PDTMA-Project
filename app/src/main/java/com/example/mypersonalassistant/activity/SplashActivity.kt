package com.example.mypersonalassistant.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.async.MainAsyncTask
import com.google.android.gms.location.*
import java.util.*

class SplashActivity : AppCompatActivity() {

    //LOCATION
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    lateinit var mLastLocation: Location
    lateinit var mLastLocationTime: Date
    private val REQUEST_PERMISSION_LOCATION = 10
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 1000

    //PERMISIONS
    private var permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.WRITE_CALENDAR,
        Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mLocationRequest = LocationRequest()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        if(checkPermission(this)){
            startLocationUpdates()
        }
        else {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }

    fun checkPermission(context: Context): Boolean {
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
                val intent = Intent(this, MainActivity::class.java)
                this.startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this@SplashActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
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
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        mLastLocationTime = Calendar.getInstance().time

        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}
