package com.example.mypersonalassistant.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.`interface`.OnItemClickListener
import com.example.mypersonalassistant.adapter.ToDoListRecycleViewAdapter
import com.example.mypersonalassistant.helper.SpeechRecognizerHelper
import com.example.mypersonalassistant.service.ToDoListService
import com.example.mypersonalassistant.model.ToDoModel

import kotlinx.android.synthetic.main.activity_to_do.*

class ToDoActivity : AppCompatActivity() {

    lateinit var toDoListService: ToDoListService

    //ADAPTER TO DO
    private var recyclerViewToDo: RecyclerView? = null
    var resultToDo: ArrayList<ToDoModel> = ArrayList<ToDoModel>()
    lateinit var adapterToDo: ToDoListRecycleViewAdapter
    lateinit var layoutManagerToDo: LinearLayoutManager

    //ADAPTER DONE
    private var recyclerViewDone: RecyclerView? = null
    var resultDone: ArrayList<ToDoModel> = ArrayList<ToDoModel>()
    lateinit var adapterDone: ToDoListRecycleViewAdapter
    lateinit var layoutManagerDone: LinearLayoutManager

    //SPEECH CODE
    private var SPEECH_REQUEST_CODE: Int = 10

    var fabAdd: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        setSupportActionBar(toolbar)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        toDoListService = ToDoListService(this)
        toDoListService.open()
        resultToDo = toDoListService.getAllToDoList()
        resultDone = toDoListService.getAllDoneList()
        toDoListService.close()

        //RECYCLE VIEW TO DO
        recyclerViewToDo = findViewById(R.id.todo_recycleview)
        adapterToDo = ToDoListRecycleViewAdapter(resultToDo, false, object : OnItemClickListener {
            override fun onItemClick(item: ToDoModel) {
                toDoListService.open()
                toDoListService.changeItemsStatus(1,item.id)
                adapterDone.list = toDoListService.getAllDoneList()
                adapterToDo.list = toDoListService.getAllToDoList()
                toDoListService.close()
                adapterToDo.notifyDataSetChanged()
                adapterDone.notifyDataSetChanged()
            }
        })
        layoutManagerToDo = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerViewToDo?.layoutManager = layoutManagerToDo
        recyclerViewToDo?.itemAnimator = DefaultItemAnimator()
        recyclerViewToDo?.adapter = adapterToDo
        adapterToDo.notifyDataSetChanged()

        //RECYCLE VIEW DONE
        recyclerViewDone = findViewById(R.id.done_recycleview)
        adapterDone = ToDoListRecycleViewAdapter(resultDone, true, object : OnItemClickListener {
            override fun onItemClick(item: ToDoModel) {
                toDoListService.open()
                toDoListService.changeItemsStatus(0,item.id)
                adapterDone.list = toDoListService.getAllDoneList()
                adapterToDo.list = toDoListService.getAllToDoList()
                toDoListService.close()
                adapterDone.notifyDataSetChanged()
                adapterToDo.notifyDataSetChanged()
            }
        })
        layoutManagerDone = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerViewDone?.layoutManager = layoutManagerDone
        recyclerViewDone?.itemAnimator = DefaultItemAnimator()
        recyclerViewDone?.adapter = adapterDone
        adapterDone.notifyDataSetChanged()

        //ADD TASK
        fabAdd = this.findViewById(R.id.fabAdd) as FloatingActionButton
        fabAdd!!.setOnClickListener {
            val inflater = this.layoutInflater
            val layout: View = inflater.inflate(R.layout.dialog_add_task,null)
            val title: EditText = layout.findViewById(R.id.taskTitle)
            val description: EditText = layout.findViewById(R.id.taskDescription)
            val builder = AlertDialog.Builder(this)
            builder.setTitle("New Task")
                .setView(layout)
                .setNegativeButton("No") { dialog, id ->
                    dialog.cancel()
                }
                .setPositiveButton("Add") { dialog, id ->
                    toDoListService.open()
                    toDoListService.addItemToDoList(title.text.toString(),description.text.toString())
                    adapterToDo.list = toDoListService.getAllToDoList()
                    toDoListService.close()
                    adapterToDo.notifyDataSetChanged()
                    dialog.cancel()
                }
            val alert: AlertDialog = builder.create()
            alert.show()
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
