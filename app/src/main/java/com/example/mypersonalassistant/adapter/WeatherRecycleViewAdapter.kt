package com.example.mypersonalassistant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.activity.CalendarActivity
import com.example.mypersonalassistant.activity.WeatherActivity
import com.example.mypersonalassistant.model.WeatherModel
import java.util.*
import java.text.SimpleDateFormat

class WeatherRecyclerViewAdapter(val list: ArrayList<WeatherModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weather = list[position % list.size]

        var day = false
        val currentTime = System.currentTimeMillis()/1000
        if(currentTime>weather.sunrise && currentTime<weather.sunset) day = true

        var holderWeather: WeatherHolder = holder as WeatherHolder

        holderWeather.cardTitleViewWeather.text = weather.name
        if(day) holderWeather.cardImageViewTitleWeather.setImageResource(R.drawable.ic_day)
        else holderWeather.cardImageViewTitleWeather.setImageResource(R.drawable.ic_night)

        holderWeather.tempratureText.text = weather.temp.toString() + " ºC"
        if(weather.temp>10) holderWeather.tempratureImage.setImageResource(R.drawable.ic_hot)
        else holderWeather.tempratureImage.setImageResource(R.drawable.ic_cold)

        holderWeather.humidityText.text = weather.humidity.toString()+" %"
        holderWeather.humidityImage.setImageResource(R.drawable.ic_humidity)

        holderWeather.windText.text = weather.windSpeed.toString() + " km/h"
        holderWeather.windImage.setImageResource(R.drawable.ic_wind)

        if(weather.weather=="Clear"){
            if(day) holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
            else holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
        }
        else if(weather.weather=="Clouds"){
            if(weather.description=="few clouds") {
                if(day) holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_day)
                else holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_night)
            }
            else holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy)
        }
        else if(weather.weather=="Rain" || weather.weather=="Drizzle"){
            holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_rain)
        }
        else if(weather.weather=="Thunderstorm"){
            holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_storm)
        }
        else if(weather.weather=="Snow"){
            holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_snowy)
        }
        else{
            holderWeather.currentWeatherMainImage.setImageResource(R.drawable.ic_fog)
        }

        holderWeather.weatherConditionMain.text = "("+weather.description+")"

        val d = Date(weather.date.toLong() * 1000)
        val f = SimpleDateFormat("dd/MM/yyyy HH:mm'h'")
        holderWeather.weatherDate.text = f.format(d)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_view_weather,parent,false)
        return WeatherRecyclerViewAdapter.WeatherHolder(itemView)
    }

    class WeatherHolder(view: View) : RecyclerView.ViewHolder(view){

        var cv: CardView

        var cardTitleViewWeather: TextView
        var cardImageViewTitleWeather: ImageView

        var tempratureText: TextView
        var tempratureImage: ImageView

        var humidityText: TextView
        var humidityImage: ImageView

        var windText: TextView
        var windImage: ImageView

        var currentWeatherMainImage: ImageView
        var weatherConditionMain: TextView

        var weatherDate: TextView

        init{
            cv = view.findViewById(R.id.weather_card_view)

            cardTitleViewWeather = itemView.findViewById(R.id.weatherHeaderMainCardtextView)
            cardImageViewTitleWeather = itemView.findViewById(R.id.weatherHeaderMainCardImageView)

            tempratureText = itemView.findViewById(R.id.tempratureMainText)
            tempratureImage = itemView.findViewById(R.id.tempratureMainImage)

            humidityText = itemView.findViewById(R.id.humidityMainText)
            humidityImage = itemView.findViewById(R.id.humidityMainImage)

            windText = itemView.findViewById(R.id.windMainText)
            windImage = itemView.findViewById(R.id.windMainImage)

            currentWeatherMainImage = itemView.findViewById(R.id.currentWeatherMainImage)
            weatherConditionMain = itemView.findViewById(R.id.weatherConditionMain)

            weatherDate = itemView.findViewById(R.id.weatherDate)

        }

    }
}