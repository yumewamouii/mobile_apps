package com.example.gps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    val LOCATION_PERM_CODE = 2
    private lateinit var locationManager: LocationManager
    private lateinit var statusTextView: TextView
    private lateinit var statusTextView2: TextView
    @SuppressLint("MissingPermission", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        statusTextView = findViewById(R.id.status)
        statusTextView2 = findViewById(R.id.status2)
        // запрашиваем разрешения на доступ к геопозиции
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // переход в запрос разрешений
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERM_CODE)
        }else {
            startLocationUpdates()
        }
        displayAvailableProviders()
        /*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

        val prv = locationManager.getBestProvider(Criteria(), true)
        Log.d("my", locationManager.allProviders.toString())
        if (prv != null) {
            val location = locationManager.getLastKnownLocation(prv)
            if (location != null)
                displayCoord(location.latitude, location.longitude)
            Log.d("mytag", "location set")
        }
        */
    }

    override fun onLocationChanged(loc: Location) {
        val lat = loc.latitude
        val lng = loc.longitude
        displayCoord(lat, lng)
        Log.d("my", "lat $lat long $lng")
    }
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    fun displayCoord(latitude: Double, longitude: Double) {
        findViewById<TextView>(R.id.lat).text = String.format("%.5f", latitude)
        findViewById<TextView>(R.id.lng).text = String.format("%.5f", longitude)
        statusTextView.text = "Online"
    }
    @SuppressLint("SetTextI18n")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERM_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                statusTextView.text = "Permission denied! Geolocation is off."
            }
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onProviderDisabled(provider: String) {
        statusTextView.text = "$provider disabled! Offline"
        Log.d("my", "$provider disabled")
    }
    @SuppressLint("SetTextI18n")
    override fun onProviderEnabled(provider: String) {
        statusTextView.text = "$provider enabled! Online"
        Log.d("my", "$provider enabled")
    }
    @SuppressLint("SetTextI18n")
    private fun displayAvailableProviders() {
        val providers = locationManager.allProviders
        statusTextView2.text = "Available providers: ${providers.joinToString(", ")}"
        Log.d("my", "Available providers: ${providers.joinToString(", ")}")
    }
    // TODO: обработать случай отключения GPS (геолокации) пользователем
    // onProviderDisabled + onProviderEnabled

    // TODO: обработать возврат в активность onRequestPermissionsResult
}