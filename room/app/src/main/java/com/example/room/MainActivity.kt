package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import android.content.Intent
import android.os.AsyncTask

class MainActivity : AppCompatActivity() {

    lateinit var db: AppDatabase
    lateinit var adapter: ResultAdapter
    var companies = mutableListOf<ResultEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "results.db"
        )
            .allowMainThreadQueries()
            .build()

        val companiesList = findViewById<RecyclerView>(R.id.companies_list)
        companiesList.layoutManager = LinearLayoutManager(this)

        AsyncTask.execute {
            val current = db.resultsDao().getAllWithoutLiveData()
            if (current.isEmpty()) {
                TestData.russianCompanies2020.forEach { db.resultsDao().insert(it) }
            }
            runOnUiThread {
                loadAndDisplay()
            }
        }

        val toDelete = findViewById<EditText>(R.id.toDelete)
        val deleteBtn = findViewById<Button>(R.id.delete)
        deleteBtn.setOnClickListener {
            val substr = toDelete.text.toString().trim()
            if (substr.isEmpty()) return@setOnClickListener
            AsyncTask.execute {
                db.resultsDao().deleteBySubstring(substr)
                runOnUiThread { loadAndDisplay() }
            }
        }

        findViewById<Button>(R.id.statistics).setOnClickListener {
            startActivity(Intent(this, StatActivity::class.java))
        }
    }


    fun loadAndDisplay() {
        AsyncTask.execute {
            val list = db.resultsDao().getAllWithoutLiveData()
            runOnUiThread {
                companies.clear()
                companies.addAll(list)
                adapter = ResultAdapter(companies)
                findViewById<RecyclerView>(R.id.companies_list).adapter = adapter
            }
        }
    }
}