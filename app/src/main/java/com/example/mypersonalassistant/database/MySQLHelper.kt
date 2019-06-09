package com.example.mypersonalassistant.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MySQLHelper(context: Context) : SQLiteOpenHelper(context, "myassistant.db", null, 1) {

    var context: Context? = context

    companion object {
        val TABLE_TO_DO = "toDoList"
        val COLUMN_ID = "id"
        val COLUMN_TITLE = "title"
        val COLUMN_DESCRIPTION = "description"
        val COLUMN_DONE = "done"
    }
    //TO DO LIST TABLE

    private val DATABASE_CREATE = ("create table " + TABLE_TO_DO + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_DONE + " integer not null"
            + ");")

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.w(MySQLHelper::class.java!!.getName(),
            "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data"
        )
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TO_DO")
        onCreate(db)
    }

}