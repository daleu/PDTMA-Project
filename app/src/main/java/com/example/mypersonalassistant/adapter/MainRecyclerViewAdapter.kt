package com.example.mypersonalassistant.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.activity.CalendarActivity
import com.example.mypersonalassistant.activity.ToDoActivity
import com.example.mypersonalassistant.activity.WeatherActivity
import com.example.mypersonalassistant.model.MainAdapterModel
import com.example.mypersonalassistant.model.WeatherModel
import java.util.*




class MainRecyclerViewAdapter(val list: ArrayList<MainAdapterModel>, val context:Context ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder.itemViewType==0){
            val weather = list[position % list.size].weather

            var day = false
            val currentTime = System.currentTimeMillis()/1000
            if(currentTime> weather!!.sunrise && currentTime<weather.sunset) day = true

            var holderWeather: MainInfoViewWeatherHolder = holder as MainInfoViewWeatherHolder

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

            holderWeather.button.setOnClickListener {
                val intent = Intent(context, WeatherActivity::class.java)
                context.startActivity(intent)
            }
            Log.i("WEATHER",weather.description)
        }
        else if(holder.itemViewType==1) {

            val toDo = list[position % list.size].toDo

            var toDoHolder: MainInfoViewToDoHolder = holder as MainInfoViewToDoHolder
            toDoHolder.button.setOnClickListener {
                val intent = Intent(context, ToDoActivity::class.java)
                context.startActivity(intent)
            }

            var adapterToDo: MainRecycleViewToDoAdapter = MainRecycleViewToDoAdapter(toDo)
            var layoutManagerToDo = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            toDoHolder.recyclerView?.layoutManager = layoutManagerToDo
            toDoHolder.recyclerView?.itemAnimator = DefaultItemAnimator()
            toDoHolder.recyclerView?.adapter = adapterToDo
            adapterToDo.notifyDataSetChanged()

            Log.i("WEATHER", toDo!!.size.toString())

            //RECYCLE VIEW TO DO
        }
        else {
            var holderCalendar: MainInfoViewCalendarHolder = holder as MainInfoViewCalendarHolder
            holderCalendar.button.setOnClickListener {
                val intent = Intent(context, CalendarActivity::class.java)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        if(list.size==0) return 0
        else return Integer.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==0){
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_weather,parent,false)
            return MainInfoViewWeatherHolder(itemView)
        }
        else if(viewType==1){
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_todo,parent,false)
            return MainInfoViewToDoHolder(itemView)
        }
        else {
            val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_calendar,parent,false)
            return MainInfoViewCalendarHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 3
    }

    class MainInfoViewCalendarHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cv: CardView
        var button: Button

        init {
            cv = view.findViewById(R.id.calendar_card_view)
            button = itemView.findViewById(R.id.explore)
        }
    }

    class MainInfoViewToDoHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cv: CardView
        var button: Button
        var recyclerView: RecyclerView

        init {
            cv = view.findViewById(R.id.calendar_card_view)
            recyclerView = view.findViewById(R.id.todo_recycleview)
            button = itemView.findViewById(R.id.explore)
        }
    }

    class MainInfoViewWeatherHolder(view: View) : RecyclerView.ViewHolder(view){

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

        var button: Button

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

            button = itemView.findViewById(R.id.explore)
        }

    }
}