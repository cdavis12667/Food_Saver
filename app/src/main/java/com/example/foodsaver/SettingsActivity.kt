package com.example.foodsaver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.ComponentActivity


class SettingsActivity : ComponentActivity() {
    //making vars
    private lateinit var settingToMainButton: android.widget.Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var settingsRadioB1: RadioButton
    private lateinit var settingsRadioB2: RadioButton
    private lateinit var settingsRadioB3: RadioButton
    private lateinit var settingsRadioB4: RadioButton
    private lateinit var settingsRadioB5: RadioButton
    private lateinit var notifyTestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        //shared preferences setup
        val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val notFreq = sharedPrefs.getInt("NotificationFrequency", 1)
        //assigning vars
        settingToMainButton = findViewById(R.id.settingToMainButton)
        radioGroup = findViewById(R.id.radioGroup)
        settingsRadioB1 = findViewById(R.id.settingsRadioB1)
        settingsRadioB2 = findViewById(R.id.settingsRadioB2)
        settingsRadioB3 = findViewById(R.id.settingsRadioB3)
        settingsRadioB4 = findViewById(R.id.settingsRadioB4)
        settingsRadioB5 = findViewById(R.id.settingsRadioB5)
        notifyTestButton = findViewById(R.id.notifyTestButton)
        //making event to switch activity
        settingToMainButton.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        //listener for radio button, setting notification preferences
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.settingsRadioB1 -> {
                editor.putInt("NotificationFrequency", 1)
                }
                R.id.settingsRadioB2 -> {
                    editor.putInt("NotificationFrequency", 2)
                }
                R.id.settingsRadioB3 -> {
                    editor.putInt("NotificationFrequency", 3)
                }
                R.id.settingsRadioB4 -> {
                    editor.putInt("NotificationFrequency", 4)
                }
                R.id.settingsRadioB5 -> {
                    editor.putInt("NotificationFrequency", 0)
                }
            }
            editor.apply()
        }
        //Test notifications
        notifyTestButton.setOnClickListener {
            val intent = Intent("pantry_update")
            if(notFreq == 0)
            {
                editor.putInt("NotificationFrequency", 1)
                sendBroadcast(intent)
                editor.putInt("NotificationFrequency", 0)
            }
            else{
                //Send the broadcast to Main
                sendBroadcast(intent)
            }





        }
    }

}

