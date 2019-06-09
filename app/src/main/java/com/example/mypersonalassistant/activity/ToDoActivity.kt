package com.example.mypersonalassistant.activity

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.adapter.ToDoListRecycleViewAdapter
import com.example.mypersonalassistant.database.ToDoListService
import com.example.mypersonalassistant.model.ToDoModel
import com.example.mypersonalassistant.model.WeatherModel

import kotlinx.android.synthetic.main.activity_to_do.*

class ToDoActivity : AppCompatActivity() {

    lateinit var toDoListService: ToDoListService
    private var recyclerView: RecyclerView? = null
    var result: ArrayList<ToDoModel> = ArrayList<ToDoModel>()
    lateinit var adapter: ToDoListRecycleViewAdapter
    lateinit var layoutManager: LinearLayoutManager

    var fabAdd: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do)
        setSupportActionBar(toolbar)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        toDoListService = ToDoListService(this)
        toDoListService.open()
        result = toDoListService.getAllToDoList()
        toDoListService.close()
        recyclerView = findViewById(R.id.todo_recycleview)
        adapter = ToDoListRecycleViewAdapter(result)
        layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

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
                    adapter.list = toDoListService.getAllToDoList()
                    toDoListService.close()
                    adapter.notifyDataSetChanged()
                    dialog.cancel()
                }
            val alert: AlertDialog = builder.create()
            alert.show()
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
