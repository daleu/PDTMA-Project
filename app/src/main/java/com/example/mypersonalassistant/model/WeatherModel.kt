package com.example.mypersonalassistant.model

import java.util.*

class WeatherModel {
    var name: String
        get() = field
        set(value) {
            field = value
        }
    var temp: Double
        get() = field
        set(value) {
            field = value
        }
    var sunrise: Int
        get() = field
        set(value) {
            field = value
        }
    var sunset: Int
        get() = field
        set(value) {
            field = value
        }
    var humidity: Int
        get() = field
        set(value) {
            field = value
        }
    var windSpeed: Double
        get() = field
        set(value) {
            field = value
        }
    var weather: String
        get() = field
        set(value) {
            field = value
        }

    var description: String
        get() = field
        set(value) {
            field = value
        }

    var date: Int
        get() = field
        set(value) {
            field = value
        }

    constructor(name: String, temp: Double, sunrise: Int, sunset:Int, humidity: Int, windSpeed: Double, weather: String, description: String, date: Int ){
        this.name = name
        this.temp = temp
        this.sunrise = sunrise
        this.sunset = sunset
        this.humidity = humidity
        this.windSpeed = windSpeed
        this.weather = weather
        this.description = description
        this.date = date
    }
}
