package com.example.mypersonalassistant.async

import android.location.Location
import android.os.AsyncTask
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class MainAsyncTask(adapter: MainRecyclerViewAdapter, location: Location) : AsyncTask<Void, WeatherModel, Void>(){

    private var adapter = adapter
    private var location = location

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherMapService()
        val data: WeatherModel = service.getCurrentWeatherByLocation(location.latitude.toFloat(),location.longitude.toFloat())
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: WeatherModel) {
        adapter.list.add(params.get(0))
        adapter.notifyDataSetChanged()
    }

}