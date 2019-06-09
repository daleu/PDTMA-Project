package com.example.mypersonalassistant.helper

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.support.annotation.RequiresApi
import android.util.Log
import java.lang.Thread.sleep
import java.util.*

class SpeechRrecognizerHelper(private var context: Context) {

    private lateinit var ttsHelper:TTSHelper

    //QUERIES
    private val myLocation = "what's my location"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun speechQuery(query: String){
        if(query==myLocation){
            val pref = context.getSharedPreferences("General",Context.MODE_PRIVATE)
            val myLocation: String = pref.getString("currentLocality","Not Found")
            ttsHelper = TTSHelper(context,myLocation)
        }
    }
}