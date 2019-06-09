package com.example.mypersonalassistant.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.model.EventModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainRecycleViewEventAdaper(var list: ArrayList<EventModel>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = list?.get(position % list!!.size)

        var holderEvent: EventHolder = holder as EventHolder
        holderEvent.cardTitle.text = event!!.title

        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm")

        val calendarBegin = Calendar.getInstance().apply {
            timeInMillis = event.begin
        }
        holderEvent.cardBegin.text = formatter.format(calendarBegin.time)

        val calendarEnd = Calendar.getInstance().apply {
            timeInMillis = event.end
        }
        holderEvent.cardEnd.text = formatter.format(calendarEnd.time)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var itemView = LayoutInflater.from(parent?.context).inflate(R.layout.card_view_event,parent,false)
        return EventHolder(itemView)
    }

    class EventHolder(view: View) : RecyclerView.ViewHolder(view){

        var cv: CardView
        var cardTitle: TextView
        var cardBegin: TextView
        var cardEnd: TextView

        init{
            cv = view.findViewById(R.id.event_card_view)
            cardTitle = itemView.findViewById(R.id.eventItem)
            cardBegin = itemView.findViewById(R.id.begin)
            cardEnd = itemView.findViewById(R.id.end)
        }
    }
}