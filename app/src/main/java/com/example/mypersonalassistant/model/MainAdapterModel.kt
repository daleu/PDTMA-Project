package com.example.mypersonalassistant.model

class MainAdapterModel {
    var type: Int
        get() = field
        set(value) {
            field = value
        }

    var weather: WeatherModel
        get() = field
        set(value) {
            field = value
        }

    var toDo: Array<ToDoModel>
        get() = field
        set(value) {
            field = value
        }

    constructor(type: Int, weather: WeatherModel, toDo: Array<ToDoModel>){
        this.type = type
        this.weather = weather
        this.toDo = toDo
    }
}