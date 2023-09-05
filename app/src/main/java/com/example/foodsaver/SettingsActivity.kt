package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        settingToMainButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }

        })
    }
}

