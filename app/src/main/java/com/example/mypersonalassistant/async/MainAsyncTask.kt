package com.example.mypersonalassistant.async

import android.location.Location
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class MainAsyncTask(adapter: MainRecyclerViewAdapter, layoutManager:LinearLayoutManager, location: Location) : AsyncTask<Void, WeatherModel, Void>(){

    private var adapter = adapter
    private var layoutManager = layoutManager
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

    override fun onPostExecute(result: Void?) {
        //super.onPostExecute(result)
        layoutManager?.scrollToPosition(Integer.MAX_VALUE / 2)
    }

}