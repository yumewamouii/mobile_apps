package com.example.recyclerviewk25

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

fun generateShades(color: Int, numberOfShades: Int): List<Int> {
    val shades = mutableListOf<Int>()

    val alpha = (color shr 24) and 0xff
    val red = (color shr 16) and 0xff
    val green = (color shr 8) and 0xff
    val blue = color and 0xff

    for (i in 0 until numberOfShades) {
        val factor = i.toFloat() / (numberOfShades - 1)
        val shadedRed = (red * (1 - factor)).toInt()
        val shadedGreen = (green * (1 - factor)).toInt()
        val shadedBlue = (blue * (1 - factor)).toInt()

        val shadedColor = (alpha shl 24) or (shadedRed shl 16) or (shadedGreen shl 8) or shadedBlue
        shades.add(shadedColor)
    }

    return shades
}

class MainActivity : AppCompatActivity() {

    //val planetsList = arrayListOf<String>("Mars", "Venus", "Earth")
    val colors = generateShades(Color.RED, 200)
    val colorsList = Array(20){ colors.random() }.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // пример использования RecyclerView с собственным адаптером
        val rv = findViewById<RecyclerView>(R.id.rview)
        val colorAdapter = ColorAdapter(LayoutInflater.from(this))
        // добавляем данные в список для отображения
        colorAdapter.submitList(colorsList)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = colorAdapter
    }
}