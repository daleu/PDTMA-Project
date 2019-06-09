package com.example.mypersonalassistant.model

import android.arch.lifecycle.Lifecycle
import android.util.EventLog

class MainAdapterModel {
    var type: Int
        get() = field
        set(value) {
            field = value
        }

    var weather: WeatherModel?
        get() = field
        set(value) {
            field = value
        }

    var toDo: ArrayList<ToDoModel>?
        get() = field
        set(value) {
            field = value
        }

    var events: ArrayList<EventModel>?
        get() = field
        set(value) {
            field = value
        }

    constructor(type: Int, weather: WeatherModel?, toDo: ArrayList<ToDoModel>?, events: ArrayList<EventModel>?){
        this.type = type
        this.weather = weather
        this.toDo = toDo
        this.events = events
    }
}