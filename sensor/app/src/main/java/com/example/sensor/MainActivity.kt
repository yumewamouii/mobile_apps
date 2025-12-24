package com.example.sensor

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorRecyclerView: RecyclerView
    private lateinit var sensorAdapter: SensorAdapter
    private lateinit var sensorCategories: Map<String, List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SensorManager::class.java)
        sensorRecyclerView = findViewById(R.id.sensorRecyclerView)
        sensorRecyclerView.layoutManager = LinearLayoutManager(this)

        sensorCategories = loadSensors()
        setupSpinner()
    }

    private fun setupSpinner() {
        val categories = sensorCategories.keys.toList()
        val spinner: Spinner = findViewById(R.id.sensorCategorySpinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                val sensors = sensorCategories[selectedCategory] ?: emptyList()
                sensorAdapter = SensorAdapter(sensors)
                sensorRecyclerView.adapter = sensorAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadSensors(): Map<String, List<String>> {
        val allSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val categories = mutableMapOf<String, MutableList<String>>()

        for (sensor in allSensors) {
            val category = when {
                sensor.type in listOf(Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_LIGHT, Sensor.TYPE_PRESSURE) -> "Датчики окружающей среды"
                sensor.type in listOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_ROTATION_VECTOR) -> "Датчики положения устройства"
                sensor.type in listOf(Sensor.TYPE_HEART_RATE, Sensor.TYPE_STEP_COUNTER, Sensor.TYPE_STEP_DETECTOR) -> "Датчики состояния человека"
                else -> "Другие"
            }

            if (categories[category] == null) {
                categories[category] = mutableListOf()
            }
            categories[category]?.add(sensor.name)
        }

        return categories
    }
}
