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
import java.util.*


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
        val date = Jobject.getInt("dt")

        val currentWeather = WeatherModel(name,temp,sunrise,sunset,humidity,windSpeed,weather,description, date)

        return currentWeather

    }

    fun getPredictionWeatherByLocation(latitude: Float, longitude: Float): ArrayList<WeatherModel>{

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("http://api.openweathermap.org/data/2.5/forecast?lat=$latitude&lon=$longitude&units=metric&APPID=$APPID").build()
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
        var responseArray = ArrayList<WeatherModel>()
        val name: String = Jobject.getJSONObject("city").getString("name")

        val jarray:JSONArray = Jobject.getJSONArray("list")
        for(i in 0 until jarray.length()){
            var weahterObject = jarray.getJSONObject(i)

            var weather: String = weahterObject.getJSONArray("weather").getJSONObject(0).getString("main")
            var description: String = weahterObject.getJSONArray("weather").getJSONObject(0).getString("description")
            var temp: Double = weahterObject.getJSONObject("main").getDouble("temp")
            var windSpeed = weahterObject.getJSONObject("wind").getDouble("speed")
            //var sunrise = weahterObject.getJSONObject("sys").getInt("sunrise")
            //var sunset = weahterObject.getJSONObject("sys").getInt("sunset")
            var humidity = weahterObject.getJSONObject("main").getInt("humidity")
            var date = weahterObject.getInt("dt")

            var currentWeather = WeatherModel(name,temp,0,0,humidity,windSpeed,weather,description,date)
            responseArray.add(currentWeather)
        }

        return responseArray

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
        val date = Jobject.getInt("dt")

        val currentWeather = WeatherModel(name,temp,sunrise,sunset,humidity,windSpeed,weather,description, date)

        return currentWeather

    }

}
