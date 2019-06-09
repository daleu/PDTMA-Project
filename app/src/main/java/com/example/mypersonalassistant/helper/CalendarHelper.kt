package com.example.mypersonalassistant.helper

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.example.mypersonalassistant.model.EventModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CalendarHelper(private var context: Context) {

    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    /*private val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Calendars._ID,                     // 0
        CalendarContract.Calendars.ACCOUNT_NAME,            // 1
        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
        CalendarContract.Calendars.OWNER_ACCOUNT            // 3
    }*/

    val EVENT_PROJECTION: Array<String> = arrayOf(
        CalendarContract.Instances.EVENT_ID,
        CalendarContract.Instances.BEGIN,
        CalendarContract.Instances.END,
        CalendarContract.Instances.TITLE
    )


    private val PROJECTION_ID_INDEX: Int = 0
    private val PROJECTION_BEGIN_INDEX: Int = 1
    private val PROJECTION_END_INDEX: Int = 2
    private val PROJECTION_TITLE_INDEX: Int = 3

    fun getTodayEvents():ArrayList<EventModel>{

        var result = ArrayList<EventModel>()
        // Specify the date range you want to search for recurring
        // event instances
        var startCalendar = Calendar.getInstance()
        startCalendar.time = Date()
        startCalendar.add(Calendar.DATE,1)
        val startMillis: Long = Calendar.getInstance().run {
            set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }
        var endCalendar = Calendar.getInstance()
        endCalendar.time = Date()
        endCalendar.add(Calendar.DATE,2)
        val endMillis: Long = Calendar.getInstance().run {
            set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val cur: Cursor = context.contentResolver.query(
            builder.build(),
            EVENT_PROJECTION,
            null,null, CalendarContract.Instances.BEGIN
        )

        while (cur.moveToNext()) {
            // Get the field values
            val beginVal: Long = cur.getLong(PROJECTION_BEGIN_INDEX)
            val endVal: Long = cur.getLong(PROJECTION_END_INDEX)
            val title: String = cur.getString(PROJECTION_TITLE_INDEX)

            var event = EventModel(title,beginVal,endVal)
            result.add(event)
        }

        return result
    }

    fun getTop100Events():ArrayList<EventModel>{
        var result = ArrayList<EventModel>()
        // Specify the date range you want to search for recurring
        // event instances
        var startCalendar = Calendar.getInstance()
        startCalendar.time = Date()
        startCalendar.add(Calendar.DATE,1)
        val startMillis: Long = Calendar.getInstance().run {
            set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }
        var endCalendar = Calendar.getInstance()
        endCalendar.time = Date()
        endCalendar.add(Calendar.YEAR,1)
        val endMillis: Long = Calendar.getInstance().run {
            set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val cur: Cursor = context.contentResolver.query(
            builder.build(),
            EVENT_PROJECTION,
            null,null, CalendarContract.Instances.BEGIN
        )

        while (cur.moveToNext() && result.size < 100) {
            // Get the field values
            val beginVal: Long = cur.getLong(PROJECTION_BEGIN_INDEX)
            val endVal: Long = cur.getLong(PROJECTION_END_INDEX)
            val title: String = cur.getString(PROJECTION_TITLE_INDEX)

            var event = EventModel(title,beginVal,endVal)
            result.add(event)
        }

        return result
    }

    fun getTop10Events():ArrayList<EventModel>{
        var result = ArrayList<EventModel>()
        // Specify the date range you want to search for recurring
        // event instances
        var startCalendar = Calendar.getInstance()
        startCalendar.time = Date()
        startCalendar.add(Calendar.DATE,1)
        val startMillis: Long = Calendar.getInstance().run {
            set(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }
        var endCalendar = Calendar.getInstance()
        endCalendar.time = Date()
        endCalendar.add(Calendar.MONTH,1)
        val endMillis: Long = Calendar.getInstance().run {
            set(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DATE), 0, 0)
            timeInMillis
        }

        // Construct the query with the desired date range.
        val builder: Uri.Builder = CalendarContract.Instances.CONTENT_URI.buildUpon()
        ContentUris.appendId(builder, startMillis)
        ContentUris.appendId(builder, endMillis)

        val cur: Cursor = context.contentResolver.query(
            builder.build(),
            EVENT_PROJECTION,
            null,null, CalendarContract.Instances.BEGIN
        )

        while (cur.moveToNext() && result.size < 10) {
            // Get the field values
            val beginVal: Long = cur.getLong(PROJECTION_BEGIN_INDEX)
            val endVal: Long = cur.getLong(PROJECTION_END_INDEX)
            val title: String = cur.getString(PROJECTION_TITLE_INDEX)

            var event = EventModel(title,beginVal,endVal)
            result.add(event)
        }

        return result
    }

    fun addEvent(){
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
        context.startActivity(intent)
    }
}