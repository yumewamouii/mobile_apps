package com.example.menu

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toolbar
import java.util.Locale

class MainActivity : ComponentActivity() {

    private var currentLocale: Locale = Locale("Rus")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadLanguagePreference()

        //val spinnerLocales: Spinner = findViewById(R.id.spinnerLocales)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.inflateMenu(R.menu.cities)
        toolbar.setOnMenuItemClickListener { item ->  Log.d("mytag", "item: $item"); true }

        val spinner: Spinner = findViewById(R.id.spinner)
        val items = arrayOf("English", "Русский", "日本語")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter
        var lang = when (currentLocale) {
            Locale.ENGLISH -> "English"
            Locale("ru") -> "Русский"
            Locale.JAPANESE -> "日本語"
            else -> "Русский"}
        spinner.setSelection(items.indexOf(lang))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Действия при выборе элемента
                val selectedItem = parent.getItemAtPosition(position)
                lang = when (currentLocale) {
                    Locale.ENGLISH -> "English"
                    Locale("ru") -> "Русский"
                    Locale.JAPANESE -> "日本語"
                    else -> "Русский"}
                if (selectedItem != lang) {
                    updateLocale(selectedItem.toString())
                    saveLanguagePreference(selectedItem.toString())
                    currentLocale = when (selectedItem) {
                        "English" -> Locale.ENGLISH
                        "Русский" -> Locale("ru")
                        "日本語" -> Locale.JAPANESE
                        else -> Locale("ru")
                    }
                    recreate()
                }

                // Выполнить действия с выбранным элементом
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Ничего не делать
            }
        }

    }
    private fun saveLanguagePreference(language: String) {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        prefs.edit().putString("app_language", language).apply()
    }

    private fun loadLanguagePreference() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val language = prefs.getString("app_language", "English") ?: "English"
        updateLocale(language)
    }

    private fun updateLocale(language: String) {
        currentLocale = when (language) {
            "English" -> Locale.ENGLISH
            "Русский" -> Locale("ru")
            "日本語" -> Locale.JAPANESE
            else -> Locale("ru")
        }
        Locale.setDefault(currentLocale)
        val configuration = resources.configuration
        configuration.setLocale(currentLocale)
        resources.updateConfiguration(configuration, resources.displayMetrics)


    }
}




