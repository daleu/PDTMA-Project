package com.example.mypersonalassistant.adapter

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.mypersonalassistant.R
import java.util.ArrayList


class MainRecyclerViewAdapter(val list: ArrayList<String?>) : RecyclerView.Adapter<MainRecyclerViewAdapter.MainInfoViewHolder>(){

    override fun onBindViewHolder(holder: MainInfoViewHolder, position: Int) {
        var userDto = list[position % list.size]
        holder.wt?.text = userDto
    }

    override fun getItemCount(): Int {
        if(list.size==0) return 0
        else return Integer.MAX_VALUE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainInfoViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_weather,parent,false)
        return MainInfoViewHolder(itemView)
    }

    /*fun getItem(position: Int): String {
        val positionInList = position % list.size
        return list.get(positionInList)
    }*/

    class MainInfoViewHolder(view: View) : RecyclerView.ViewHolder(view){

        var cv: CardView?  = null
        var wt: TextView? = null

        init{
            cv = view.findViewById(R.id.weather_card_view)
            wt = itemView.findViewById(R.id.weather_card_title)
        }

    }
}