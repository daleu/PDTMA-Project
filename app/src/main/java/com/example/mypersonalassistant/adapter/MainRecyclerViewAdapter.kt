package com.example.mypersonalassistant.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.model.WeatherModel
import java.util.*


class MainRecyclerViewAdapter(val list: ArrayList<WeatherModel>) : RecyclerView.Adapter<MainRecyclerViewAdapter.MainInfoViewHolder>(){

    override fun onBindViewHolder(holder: MainInfoViewHolder, position: Int) {
        val weather = list[position % list.size]

        var day = false
        val currentTime = System.currentTimeMillis()/1000
        if(currentTime>weather.sunrise && currentTime<weather.sunset) day = true

        holder.cardTitleViewWeather.text = weather.name
        if(day) holder.cardImageViewTitleWeather.setImageResource(R.drawable.ic_day)
        else holder.cardImageViewTitleWeather.setImageResource(R.drawable.ic_night)

        holder.tempratureText.text = weather.temp.toString() + " Cº"
        if(weather.temp>10) holder.tempratureImage.setImageResource(R.drawable.ic_hot)
        else holder.tempratureImage.setImageResource(R.drawable.ic_cold)

        holder.humidityText.text = weather.humidity.toString()+" %"
        holder.humidityImage.setImageResource(R.drawable.ic_humidity)

        holder.windText.text = weather.windSpeed.toString() + " km/h"
        holder.windImage.setImageResource(R.drawable.ic_wind)

        if(weather.weather=="Clear"){
            if(day) holder.currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
            else holder.currentWeatherMainImage.setImageResource(R.drawable.ic_clear_day)
        }
        else if(weather.weather=="Clouds"){
            if(weather.description=="few clouds") {
                if(day) holder.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_day)
                else holder.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy_half_night)
            }
            else holder.currentWeatherMainImage.setImageResource(R.drawable.ic_cloudy)
        }
        else if(weather.weather=="Rain" || weather.weather=="Drizzle"){
            holder.currentWeatherMainImage.setImageResource(R.drawable.ic_rain)
        }
        else if(weather.weather=="Thunderstorm"){
            holder.currentWeatherMainImage.setImageResource(R.drawable.ic_storm)
        }
        else if(weather.weather=="Snow"){
            holder.currentWeatherMainImage.setImageResource(R.drawable.ic_snowy)
        }
        else{
            holder.currentWeatherMainImage.setImageResource(R.drawable.ic_fog)
        }

        holder.weatherConditionMain.text = "("+weather.description+")"

    }

    override fun getItemCount(): Int {
        if(list.size==0) return 0
        else return Integer.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainInfoViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_weather,parent,false)
        return MainInfoViewHolder(itemView)
    }

    class MainInfoViewHolder(view: View) : RecyclerView.ViewHolder(view){

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
        }

    }
}