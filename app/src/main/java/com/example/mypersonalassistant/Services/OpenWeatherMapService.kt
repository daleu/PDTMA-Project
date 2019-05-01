package com.example.mypersonalassistant.Services

import com.example.mypersonalassistant.BuildConfig
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException

class OpenWeatherMapService {

    private val APPID: String = BuildConfig.OpenWeatherMapAPIKey

    fun getCurrentWeatherByCityName(city: String): String?{

        val client: OkHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q=London&APPID=e2b9357594875a9a24508547112f381c").build()
        var response: Response? = null

        try {
            response = client.newCall(request).execute()
            return response?.body().toString()
        } catch (e: IOException){
            e.printStackTrace()
        }

        return null

    }

}