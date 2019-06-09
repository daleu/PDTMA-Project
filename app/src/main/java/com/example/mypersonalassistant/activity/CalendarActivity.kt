package com.example.mypersonalassistant.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.helper.SpeechRrecognizerHelper

import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    //SPEECH CODE
    private var SPEECH_REQUEST_CODE: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        fab.setOnClickListener { view ->
            displaySpeechRecognizer()
        }

        var calId = 1
        var cal: Calendar = Calendar.getInstance()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displaySpeechRecognizer(){
        var intent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var results: List<String> = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            var spokenText = results.get(0)
            val speechRrecognizerHelper = SpeechRrecognizerHelper(this)
            speechRrecognizerHelper.speechQuery(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
