package com.example.mypersonalassistant.async

import android.location.Location
import android.os.AsyncTask
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.adapter.WeatherRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherAsyncTask(
        private var cv: CardView,
        private var cardTitleViewWeather: TextView,
        private var cardImageViewTitleWeather: ImageView,
        private var tempratureText: TextView,
        private var tempratureImage: ImageView,
        private var humidityText: TextView,
        private var humidityImage: ImageView,
        private var windText: TextView,
        private var windImage: ImageView,
        private var currentWeatherMainImage: ImageView,
        private var weatherConditionMain: TextView,
        private var weatherDate: TextView,
        location: Location) : AsyncTask<Void, WeatherModel, Void>(){

    private var location = location

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherMapService()
        val data: WeatherModel = service.getCurrentWeatherByLocation(location.latitude.toFloat(),location.longitude.toFloat())
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: WeatherModel) {

        var day = false
        val currentTime = System.currentTimeMillis()/1000
        if(currentTime>params.get(0).sunrise && currentTime<params.get(0).sunset) day = true

        cardTitleViewWeather.text = params.get(0).name
        if(day) cardImageViewTitleWeather.setImageResource(R.drawable.ic_day)
        else cardImageViewTitleWeather.setImageResource(R.drawable.ic_night)

        tempratureText.text = params.get(0).temp.toString() + " ºC"
        if(params.get(0).temp>10) tempratureImage.setImageResource(R.drawable.ic_hot)
        else tempratureImage.setImageResource(R.drawable.ic_cold)

        humidityText.text = params.get(0).humidity.toString()+" %"
        humidityImage.setImageResource(R.drawable.ic_humidity)

        windText.text = params.get(0).windSpeed.toString() + " km/h"
        windImage.setImageResource(R.drawable.ic_wind)

        if(params.get(0).weather=="Clear"){
            if(day) currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
            else currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
        }
        else if(params.get(0).weather=="Clouds"){
            if(params.get(0).description=="few clouds") {
                if(day) currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_day)
                else currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_night)
            }
            else currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy)
        }
        else if(params.get(0).weather=="Rain" || params.get(0).weather=="Drizzle"){
            currentWeatherMainImage.setImageResource(R.drawable.ic_rain)
        }
        else if(params.get(0).weather=="Thunderstorm"){
            currentWeatherMainImage.setImageResource(R.drawable.ic_storm)
        }
        else if(params.get(0).weather=="Snow"){
            currentWeatherMainImage.setImageResource(R.drawable.ic_snowy)
        }
        else{
            currentWeatherMainImage.setImageResource(R.drawable.ic_fog)
        }

        weatherConditionMain.text = "("+params.get(0).description+")"

        val d = Date(params.get(0).date.toLong() * 1000)
        val f = SimpleDateFormat("dd/MM/yyyy HH:mm'h'")
        weatherDate.text = f.format(d)
    }
}