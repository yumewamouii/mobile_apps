package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import android.os.AsyncTask

class StatActivity : AppCompatActivity() {

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stat)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "results.db"
        ).allowMainThreadQueries().build()

        AsyncTask.execute {
            val all = db.resultsDao().getAllWithoutLiveData()
            val capList = all.mapNotNull { it.result }
            val names = all.mapNotNull { it.name }

            val totalCap = capList.sum()
            val avgCap = if (capList.isNotEmpty()) capList.average() else 0.0
            val aboveAvg = capList.count { it > avgCap }

            val regexEng = Regex("^[A-Za-z0-9.,&«»\\-()\\s]+$")
            val englishCount = all.count { it.name?.matches(regexEng) == true }

            val topCap = capList.maxOrNull() ?: 0
            val topCompanies = all.filter { it.result == topCap && it.name != null }
            val best = topCompanies.minByOrNull { it.name!! }?.name ?: "-"

            val maxNameLen = names.maxOfOrNull { it.length } ?: 0
            val longCompanies = all.filter { (it.name?.length == maxNameLen) && it.name != null }
            val longest = longCompanies.minByOrNull { it.name!! }?.name ?: "-"

            runOnUiThread {
                findViewById<TextView>(R.id.money).text = totalCap.toString()
                findViewById<TextView>(R.id.good).text = aboveAvg.toString()
                findViewById<TextView>(R.id.english).text = englishCount.toString()
                findViewById<TextView>(R.id.best).text = best
                findViewById<TextView>(R.id.longest).text = longest
            }
        }
    }
}