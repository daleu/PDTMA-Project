package com.example.mypersonalassistant.async

import android.location.Location
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.adapter.WeatherRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class WeatherAsyncTask(adapter: WeatherRecyclerViewAdapter, location: Location) : AsyncTask<Void, ArrayList<WeatherModel>, Void>(){

    private var adapter = adapter
    private var location = location

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherMapService()
        val data: ArrayList<WeatherModel> = service.getPredictionWeatherByLocation(location.latitude.toFloat(),location.longitude.toFloat())
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: ArrayList<WeatherModel>) {
        for(i in 0 until params[0].size){
            adapter.list.add(params[0][i])
        }
        adapter.notifyDataSetChanged()
    }
}