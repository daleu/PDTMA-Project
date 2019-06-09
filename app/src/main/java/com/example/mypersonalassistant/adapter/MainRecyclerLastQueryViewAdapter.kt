package com.example.mypersonalassistant.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.model.QueryModel
import com.example.mypersonalassistant.model.WeatherModel
import java.util.*




class MainRecyclerLastQueryViewAdapter(val list: ArrayList<QueryModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val query = list[position % list.size]

        var holderLastQuery: MainInfoLastQueryHolder = holder as MainInfoLastQueryHolder

        holderLastQuery.lastQueryTitle.text = query.title
        holderLastQuery.lastQueryDescription.text = query.description
        holderLastQuery.lastQueryImageView.setImageResource(R.drawable.ic_clear_day)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.content_main_card_view_last_query,parent,false)
        return MainInfoLastQueryHolder(itemView)
    }

    class MainInfoLastQueryHolder(view: View) : RecyclerView.ViewHolder(view){

        var cv: CardView

        var lastQueryTitle: TextView
        var lastQueryImageView: ImageView

        var lastQueryDescription: TextView

        init{
            cv = view.findViewById(R.id.lastquery_card_view)

            lastQueryTitle = itemView.findViewById(R.id.lastQueryTitle)
            lastQueryImageView = itemView.findViewById(R.id.lastQueryImageView)

            lastQueryDescription = itemView.findViewById(R.id.lastQueryDescription)
        }

    }
}