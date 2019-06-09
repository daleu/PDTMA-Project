package com.example.mypersonalassistant.model

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

    constructor(type: Int, weather: WeatherModel?, toDo: ArrayList<ToDoModel>?){
        this.type = type
        this.weather = weather
        this.toDo = toDo
    }
}