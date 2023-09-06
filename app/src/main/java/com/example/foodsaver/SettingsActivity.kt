package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity


class SettingsActivity : ComponentActivity() {
    //making vars
    private lateinit var settingToMainButton: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        //assigning vars
        settingToMainButton = findViewById(R.id.settingToMainButton)
        //making event to switch activity
        settingToMainButton.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
    }
}

