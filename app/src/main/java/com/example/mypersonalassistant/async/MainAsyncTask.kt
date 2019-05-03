package com.example.mypersonalassistant.async

import android.os.AsyncTask
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class MainAsyncTask(adapter: MainRecyclerViewAdapter) : AsyncTask<Void, WeatherModel, Void>(){

    var adapter = adapter

    override fun doInBackground(vararg params: Void?): Void? {
        val service: OpenWeatherMapService = OpenWeatherMapService()
        val data: WeatherModel = service.getCurrentWeatherByCityName("London")
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: WeatherModel) {
        adapter.list.add(params.get(0))
        adapter.notifyDataSetChanged()
    }

}