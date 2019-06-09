package com.example.mypersonalassistant.helper

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.support.annotation.RequiresApi
import android.util.Log
import java.lang.Thread.sleep
import java.util.*

class SpeechRecognizerHelper(private var context: Context) {

    private lateinit var ttsHelper:TTSHelper

    //QUERIES
    private val myLocation = "what's my location"
    private val eventsForToday = "show me the events for today"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speechQuery(query: String){
        if(query==myLocation){
            val pref = context.getSharedPreferences("General",Context.MODE_PRIVATE)
            val myLocation: String = pref.getString("currentLocality","Not Found")
            ttsHelper = TTSHelper(context,myLocation)
        }
        else if(query==eventsForToday){
            val calendarHelper = CalendarHelper(context)
            calendarHelper.getTodayEvents()
        }
    }
}