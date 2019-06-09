package com.example.mypersonalassistant.async

import android.content.Context
import android.location.Location
import android.os.AsyncTask
import android.support.v7.widget.LinearLayoutManager
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import com.example.mypersonalassistant.helper.CalendarHelper
import com.example.mypersonalassistant.service.ToDoListService
import com.example.mypersonalassistant.model.MainAdapterModel
import com.example.mypersonalassistant.model.WeatherModel
import com.example.mypersonalassistant.service.OpenWeatherMapService

class MainAsyncTask(adapter: MainRecyclerViewAdapter, layoutManager:LinearLayoutManager, location: Location, context: Context) : AsyncTask<Void, ArrayList<MainAdapterModel>, Void>(){

    private var adapter = adapter
    private var layoutManager = layoutManager
    private var location = location
    private var toDoListService: ToDoListService =
        ToDoListService(context)
    private var calendarHelper: CalendarHelper =
        CalendarHelper(context)

    override fun doInBackground(vararg params: Void?): Void? {
        val service = OpenWeatherMapService()
        val data: WeatherModel = service.getCurrentWeatherByLocation(location.latitude.toFloat(),location.longitude.toFloat())
        val dataWeather = MainAdapterModel(0,data, null, null)
        val arrayList = ArrayList<MainAdapterModel>()
        arrayList.add(dataWeather)
        publishProgress(arrayList)
        return null
    }

    override fun onProgressUpdate(vararg params: ArrayList<MainAdapterModel>) {
        //ADD WEATHER
        adapter.list = ArrayList<MainAdapterModel>()
        adapter.list.add(params[0][0])

        //ADD TO DO
        toDoListService.open()
        var resultToDo = toDoListService.getAllToDoList()
        toDoListService.close()
        val dataToDo = MainAdapterModel(1,null, resultToDo, null)
        adapter.list.add(dataToDo)

        //ADD CALENDAR
        val resultEvents = calendarHelper.getTop10Events()
        val dataEvents = MainAdapterModel(2,null,null, resultEvents)
        adapter.list.add(dataEvents)
        adapter.notifyDataSetChanged()
    }

    override fun onPostExecute(result: Void?) {
        //super.onPostExecute(result)
        layoutManager?.scrollToPosition(Integer.MAX_VALUE / 2)
    }

}