package com.example.mypersonalassistant.activity

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.mypersonalassistant.BuildConfig
import com.example.mypersonalassistant.R
import com.example.mypersonalassistant.Services.OpenWeatherMapService
import com.example.mypersonalassistant.adapter.MainRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.ref.WeakReference
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var recyclerView: RecyclerView? = null
    var result: ArrayList<String?> = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //RECYCLE VIEW
        recyclerView = findViewById(R.id.main_recycleview)
        var adapter = MainRecyclerViewAdapter(result)
        val layoutManager = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.layoutManager?.scrollToPosition(Integer.MAX_VALUE / 2)

        recyclerView?.adapter = adapter
        adapter.notifyDataSetChanged()

        //FLOATING BUTTON
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val myTask = MyAsyncTask(adapter)
        myTask.execute()

    }

    class MyAsyncTask(adapter: MainRecyclerViewAdapter) : AsyncTask<Void, String, Void>(){

        var adapter = adapter

        override fun doInBackground(vararg params: Void?): Void? {
            val service: OpenWeatherMapService = OpenWeatherMapService()
            val data = service.getCurrentWeatherByCityName("London")
            publishProgress(data)
            return null
        }

        override fun onProgressUpdate(vararg params: String?) {
            adapter.list.add(params.toString())
            adapter.notifyDataSetChanged()
        }

    }


    private fun generateData(): ArrayList<String?> {

        var result = ArrayList<String?>()

        for (i in 0..9){
            result.add(BuildConfig.OpenWeatherMapAPIKey)
        }

        return result

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
