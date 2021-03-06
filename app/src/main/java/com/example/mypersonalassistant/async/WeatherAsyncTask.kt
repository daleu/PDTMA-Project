package com.example.mypersonalassistant.async

import android.location.Location
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.adapter.WeatherRecyclerViewAdapter
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class WeatherAsyncTask(private var adapter: WeatherRecyclerViewAdapter, private var location: Location?, private var locationString: String?) : AsyncTask<Void, ArrayList<WeatherModel>, Void>(){

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherMapService()
        val data: ArrayList<WeatherModel>
        if(location==null) {
            data=service.getPredictionWeatherByCityName(locationString!!)
        }
        else{
            data=service.getPredictionWeatherByLocation(location!!.latitude.toFloat(),location!!.longitude.toFloat())
        }
        publishProgress(data)
        return null
    }

    override fun onProgressUpdate(vararg params: ArrayList<WeatherModel>) {
        adapter.list = ArrayList<WeatherModel>()
        for(i in 0 until params[0].size){
            adapter.list.add(params[0][i])
        }
        adapter.notifyDataSetChanged()
    }
}