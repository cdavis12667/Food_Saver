package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity


class SettingsActivity : ComponentActivity() {
    //making vars
    private lateinit var setting_home:android.widget.ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        //assigning vars
        setting_home = findViewById(R.id.setting_home)
        //making event to switch activity
        setting_home.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
    }
}

