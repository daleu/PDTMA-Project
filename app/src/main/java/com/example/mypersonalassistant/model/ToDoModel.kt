package com.example.mypersonalassistant.model

class ToDoModel {

    var id: Int
        get() = field
        set(value) {
            field = value
        }

    var title: String
        get() = field
        set(value) {
            field = value
        }
    var description: String
        get() = field
        set(value) {
            field = value
        }
    var done: Int
        get() = field
        set(value) {
            field = value
        }

    constructor(id: Int, title: String, description: String, done: Int ){
        this.id = id
        this.title = title
        this.description = description
        this.done = done
    }
}