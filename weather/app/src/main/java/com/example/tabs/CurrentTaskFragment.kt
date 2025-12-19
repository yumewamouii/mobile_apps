package com.example.tabs


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentTaskFragment: Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_current_task, container, false)
        view.setBackgroundColor(Color.LTGRAY)

        val API_KEY="d6843ab8ee963f5d372296dfff62aed7"
        val city="Irkutsk"
        Log.d("LOGGED IN: ", "CORRECT")
        RetrofitClient.instance.getWeather(city, API_KEY).enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val location= it.name
                        val temperature = it.main.temp.toString()
                        val wind = it.wind.speed.toString()
                        val humidity=it.main.humidity.toString()
                        val weather = it.weather[0].description
                        val feels_like=it.main.feels_like
                        val pressure = it.main.pressure

                        view.findViewById<TextView>(R.id.location).text= "Город: $location"
                        view.findViewById<TextView>(R.id.temperature).text= "Температура: $temperature ℃"
                        view.findViewById<TextView>(R.id.wind).text= "Направление ветра: $wind м/с"
                        view.findViewById<TextView>(R.id.humidity).text= "Влажность: $humidity кг/м³"
                        view.findViewById<TextView>(R.id.weather).text= "Погода: $weather"
                        view.findViewById<TextView>(R.id.feels_like).text= "Ощущается как: $feels_like ℃"
                        view.findViewById<TextView>(R.id.pressure).text= "Давление: $pressure Па"

                    }
                } else {
                    Log.e("Weather", "Request failed with code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("Weather", "Error: ${t.message}")
            }
        })

        return view
        //return super.onCreateView(inflater, container, savedInstanceState)
    }
}