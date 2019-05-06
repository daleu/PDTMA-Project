package com.example.mypersonalassistant.service

import com.example.mypersonalassistant.BuildConfig
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import android.R.string
import com.example.mypersonalassistant.model.WeatherModel
import org.json.JSONArray
import org.json.JSONObject


class OpenWeatherMapService {

    private val APPID: String = BuildConfig.OpenWeatherMapAPIKey

    fun getCurrentWeatherByCityName(city: String): WeatherModel{

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&APPID=$APPID").build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
            //return response?.body().toString()
        } catch (e: IOException){
            e.printStackTrace()
        }

        val jsonData = response?.body()?.string()
        val Jobject  = JSONObject(jsonData)

        //parse Weather Data & create WeatherModel Object
        val name: String = Jobject.getString("name")
        val weather: String = Jobject.getJSONArray("weather").getJSONObject(0).getString("main")
        val description: String = Jobject.getJSONArray("weather").getJSONObject(0).getString("description")
        val temp: Double = Jobject.getJSONObject("main").getDouble("temp")
        val windSpeed = Jobject.getJSONObject("wind").getDouble("speed")
        val sunrise = Jobject.getJSONObject("sys").getInt("sunrise")
        val sunset = Jobject.getJSONObject("sys").getInt("sunset")
        val humidity = Jobject.getJSONObject("main").getInt("humidity")

        val currentWeather = WeatherModel(name,temp,sunrise,sunset,humidity,windSpeed,weather,description)

        return currentWeather

    }

    fun getCurrentWeatherByLocation(latitude: Float, longitude: Float): WeatherModel{

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&units=metric&APPID=$APPID").build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
            //return response?.body().toString()
        } catch (e: IOException){
            e.printStackTrace()
        }

        val jsonData = response?.body()?.string()
        val Jobject  = JSONObject(jsonData)

        //parse Weather Data & create WeatherModel Object
        val name: String = Jobject.getString("name")
        val weather: String = Jobject.getJSONArray("weather").getJSONObject(0).getString("main")
        val description: String = Jobject.getJSONArray("weather").getJSONObject(0).getString("description")
        val temp: Double = Jobject.getJSONObject("main").getDouble("temp")
        val windSpeed = Jobject.getJSONObject("wind").getDouble("speed")
        val sunrise = Jobject.getJSONObject("sys").getInt("sunrise")
        val sunset = Jobject.getJSONObject("sys").getInt("sunset")
        val humidity = Jobject.getJSONObject("main").getInt("humidity")

        val currentWeather = WeatherModel(name,temp,sunrise,sunset,humidity,windSpeed,weather,description)

        return currentWeather

    }

}
