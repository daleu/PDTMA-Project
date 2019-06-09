package com.example.mypersonalassistant.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.example.mypersonalassistant.model.ToDoModel

class ToDoListService(context: Context) {

    // Database fields
    private var database: SQLiteDatabase? = null
    private var dbHelper: MySQLHelper = MySQLHelper(context)

    private var allColumns = arrayOf(
        MySQLHelper.COLUMN_ID,
        MySQLHelper.COLUMN_TITLE,
        MySQLHelper.COLUMN_DESCRIPTION,
        MySQLHelper.COLUMN_DONE)

    @Throws(SQLException::class)
    fun open() {
        database = dbHelper.writableDatabase
    }

    fun close() {
        dbHelper.close()
    }

    fun addItemToDoList(title:String,description:String): Long {
        val values = ContentValues()
        values.put(MySQLHelper.COLUMN_TITLE,title)
        values.put(MySQLHelper.COLUMN_DESCRIPTION,description)
        values.put(MySQLHelper.COLUMN_DONE,0)

        val insertId:Long = this.database!!.insert(MySQLHelper.TABLE_TO_DO,null,values)
        return insertId
    }

    fun changeItemsStatus(done: Int, id:Int){
        val values = ContentValues()
        values.put(MySQLHelper.COLUMN_DONE,done)

        database!!.update(MySQLHelper.TABLE_TO_DO, values, MySQLHelper.COLUMN_ID + "=" + id, null)
    }

    @SuppressLint("Recycle")
    fun getAllToDoList():ArrayList<ToDoModel>{
        val toDoList = ArrayList<ToDoModel>()

        val cursor: Cursor = database!!.query(MySQLHelper.TABLE_TO_DO,allColumns,MySQLHelper.COLUMN_DONE+"=0",null,null,null,MySQLHelper.COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            val todo:ToDoModel = cursorToToDo(cursor)
            toDoList.add(todo)
            cursor.moveToNext()
        }
        cursor.close()
        return toDoList
    }

    @SuppressLint("Recycle")
    fun getTop10ToDoList():ArrayList<ToDoModel>{
        val toDoList = ArrayList<ToDoModel>()

        val cursor: Cursor = database!!.query(MySQLHelper.TABLE_TO_DO,allColumns,MySQLHelper.COLUMN_DONE+"=0",null,null,null,MySQLHelper.COLUMN_ID,"10")

        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            val todo:ToDoModel = cursorToToDo(cursor)
            toDoList.add(todo)
            cursor.moveToNext()
        }
        cursor.close()
        return toDoList
    }

    @SuppressLint("Recycle")
    fun getAllDoneList():ArrayList<ToDoModel>{
        val toDoList = ArrayList<ToDoModel>()

        val cursor: Cursor = database!!.query(MySQLHelper.TABLE_TO_DO,allColumns,MySQLHelper.COLUMN_DONE+"=1",null,null,null,MySQLHelper.COLUMN_ID)

        cursor.moveToFirst()
        while (!cursor.isAfterLast){
            val todo:ToDoModel = cursorToToDo(cursor)
            toDoList.add(todo)
            cursor.moveToNext()
        }
        cursor.close()
        return toDoList
    }

    private fun cursorToToDo(cursor: Cursor):ToDoModel{
        return ToDoModel(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getInt(3)
        )
    }
}