package com.example.mypersonalassistant.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.model.ToDoModel

class ToDoListRecycleViewAdapter(var list: ArrayList<ToDoModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val toDo = list[position % list.size]

        var holderToDo: ToDoHolder = holder as ToDoHolder
        holderToDo.cardTitle.text = toDo.title
        holderToDo.cardDescription.text = toDo.description
        holderToDo.toDoId = toDo.id
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_view_todo,parent,false)
        return ToDoHolder(itemView)
    }


    class ToDoHolder(view: View) : RecyclerView.ViewHolder(view){

        var cv: CardView
        var cardTitle: TextView
        var cardDescription: TextView
        var toDoId: Int = 0
        var button: Button

        init{
            cv = view.findViewById(R.id.todo_card_view)

            cardTitle = itemView.findViewById(R.id.todoItem)
            cardDescription = itemView.findViewById(R.id.todoDescription)
            button = itemView.findViewById(R.id.checkTask)
        }
    }
}