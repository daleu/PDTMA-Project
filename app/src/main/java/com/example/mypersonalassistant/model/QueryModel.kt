package com.example.mypersonalassistant.model

class QueryModel {

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

    var tag: String
        get() = field
        set(value) {
            field = value
        }


    constructor(id: Int, title: String, description: String, tag: String ){
        this.id = id
        this.title = title
        this.description = description
        this.tag = tag
    }
}
