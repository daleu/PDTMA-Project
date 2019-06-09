package com.example.mypersonalassistant.model

class EventModel {

    var title: String
        get() = field
        set(value) {
            field = value
        }
    var begin: Long
        get() = field
        set(value) {
            field = value
        }
    var end: Long
        get() = field
        set(value) {
            field = value
        }

    constructor(title: String, begin: Long, end: Long ){
        this.title = title
        this.begin = begin
        this.end = end
    }
}