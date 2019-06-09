package com.example.mypersonalassistant.helper

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.support.annotation.RequiresApi
import android.util.Log
import com.example.mypersonalassistant.activity.MainActivity
import com.example.mypersonalassistant.activity.WeatherActivity
import android.app.Activity
import com.example.mypersonalassistant.activity.ToDoActivity
import com.example.mypersonalassistant.service.ToDoListService


class SpeechRecognizerHelper(private var context: Context) {

    private lateinit var ttsHelper:TTSHelper
    private var toDoListService = ToDoListService(context)
    private var calendarHelper = CalendarHelper(context)

    //QUERIES
    private val myLocation = "what's my location"
    private val wheatherForecast = "what's the weather in "
    private val createTask = "create task "
    private val eventsForToday = "show me the events for today"
    private val createEvent = "create event"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speechQuery(query: String){
        if(query==myLocation){
            val pref = context.getSharedPreferences("General",Context.MODE_PRIVATE)
            val myLocation: String = pref.getString("currentLocality","Not Found")
            ttsHelper = TTSHelper(context,"You are in "+myLocation)
        }
        else if(query==eventsForToday){
            val calendarHelper = CalendarHelper(context)
            var events = calendarHelper.getTodayEvents()
            var textToSpeach = "The events for today are:"
            for(event in events){
                textToSpeach = textToSpeach + event.title + ","
            }
            ttsHelper = TTSHelper(context,"You are in "+textToSpeach)
        }
        else if (query.contains(wheatherForecast)){
            var city = query.replace(wheatherForecast,"")

            val pref = context.getSharedPreferences("Cities",Context.MODE_PRIVATE).edit()
            pref.putString(city,city).apply()
            val prefGen = context.getSharedPreferences("General", ContextWrapper.MODE_PRIVATE).edit()
            prefGen.putString("selectedLocation",city).apply()

            ttsHelper = TTSHelper(context,"Here is the weather in"+city )

            val intent = Intent(context, WeatherActivity::class.java)
            context.startActivity(intent)
            if(context !is MainActivity ) (context as Activity).finish()
        }
        else if (query.contains(createTask)){
            var title = query.replace(createTask,"")

            toDoListService.open()
            toDoListService.addItemToDoList(title,title)
            toDoListService.close()

            val intent = Intent(context, ToDoActivity::class.java)
            context.startActivity(intent)
            if(context !is MainActivity ) (context as Activity).finish()
        }
        else if (query.contains(createEvent)){
            calendarHelper.addEvent()
        }
        val prefGen = context.getSharedPreferences("General", ContextWrapper.MODE_PRIVATE).edit()
        prefGen.putString("selectedLocation",query).apply()
    }
}