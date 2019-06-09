package com.example.mypersonalassistant.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.CalendarView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.adapter.ToDoListRecycleViewAdapter
import com.example.mypersonalassistant.helper.CalendarHelper
import com.example.mypersonalassistant.helper.SpeechRecognizerHelper
import com.example.mypersonalassistant.model.ToDoModel

import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    //CALENDAR ADAPTER
    private var recyclerViewToDo: RecyclerView? = null
    //var resultToDo: ArrayList<EventModel> = ArrayList<EventModel>()
    lateinit var adapterToDo: ToDoListRecycleViewAdapter
    lateinit var layoutManagerToDo: LinearLayoutManager

    //CALENDAR HELPER
    private var calendarHelper = CalendarHelper(this)

    //SPEECH CODE
    private var SPEECH_REQUEST_CODE: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        fabAdd!!.setOnClickListener {view ->
            calendarHelper.addEvent()
        }

        fab.setOnClickListener { view ->
            displaySpeechRecognizer()
        }
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
            val speechRrecognizerHelper = SpeechRecognizerHelper(this)
            speechRrecognizerHelper.speechQuery(spokenText)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
