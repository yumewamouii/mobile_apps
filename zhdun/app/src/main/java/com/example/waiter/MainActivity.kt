package com.example.waiter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvTimeCounter: TextView
    private lateinit var btnStopWaiting: Button

    private var minutesPassed = 0
    private var isListening = false

    private val timeReceiver = object : BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIME_TICK) {
                minutesPassed++
                tvTimeCounter.text = "Время созерцания: $minutesPassed мин."
            }
        }
    }

    private val batteryReceiver = object : BroadcastReceiver() {
        private var isCharging = false

        override fun onReceive(context: Context?, intent: Intent?) {
            val status: Int = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val currentlyCharging = (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL)

            if (currentlyCharging && !isCharging) {
                isCharging = true
                Toast.makeText(context, "Устройство начало заряжаться", Toast.LENGTH_SHORT).show()
            } else if (!currentlyCharging && isCharging) {
                isCharging = false
                Toast.makeText(context, "Устройство перестало заряжаться", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTimeCounter = findViewById(R.id.tv_time_counter)
        btnStopWaiting = findViewById(R.id.btn_stop_waiting)

        btnStopWaiting.setOnClickListener {
            stopListening()
            Toast.makeText(this, "Дождались!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        startListening()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onPause() {
        super.onPause()
        stopListening()
        unregisterReceiver(batteryReceiver)
    }

    private fun startListening() {
        if (!isListening) {
            registerReceiver(timeReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
            isListening = true
        }
    }

    private fun stopListening() {
        if (isListening) {
            unregisterReceiver(timeReceiver)
            isListening = false
        }
    }
}
