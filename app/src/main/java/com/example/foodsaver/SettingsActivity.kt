package com.example.foodsaver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Switch
import androidx.activity.ComponentActivity


class SettingsActivity : ComponentActivity() {
    //making vars

    private lateinit var radioGroup: RadioGroup
    private lateinit var settingsRadioB1: RadioButton
    private lateinit var settingsRadioB2: RadioButton
    private lateinit var settingsRadioB3: RadioButton
    private lateinit var settingsRadioB4: RadioButton
    private lateinit var settingsRadioB5: RadioButton
    private lateinit var settingsSwitch1: Switch
    private lateinit var settingsSwitch2: Switch
    private lateinit var notifyTestButton: Button
    private lateinit var setting_home:android.widget.ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        //shared preferences setup
        val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val radioButtonSelection = sharedPrefs.getInt("FreqButton", -1)
        val notFreq = sharedPrefs.getInt("NotificationFrequency", 1)
        val expDailyCheck = sharedPrefs.getBoolean("DailyExpCheck", false)
        val missingDateNotify = sharedPrefs.getBoolean("SkipMissingDateCheck", false)

        //assigning vars
        radioGroup = findViewById(R.id.radioGroup)
        settingsRadioB1 = findViewById(R.id.settingsRadioB1)
        settingsRadioB2 = findViewById(R.id.settingsRadioB2)
        settingsRadioB3 = findViewById(R.id.settingsRadioB3)
        settingsRadioB4 = findViewById(R.id.settingsRadioB4)
        settingsRadioB5 = findViewById(R.id.settingsRadioB5)
        settingsSwitch1 = findViewById(R.id.settingsSwitch1)
        settingsSwitch2 = findViewById(R.id.settingsSwitch2)
        notifyTestButton = findViewById(R.id.notifyTestButton)
        setting_home = findViewById(R.id.setting_home)
        //making event to switch activity
        setting_home.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        //load user selection
        if (radioButtonSelection != -1) {
            radioGroup.check(radioButtonSelection)
        }
        settingsSwitch1.isChecked = expDailyCheck
        settingsSwitch2.isChecked = missingDateNotify


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
            editor.putInt("FreqButton", checkedId)
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

        settingsSwitch1.setOnCheckedChangeListener{_,isChecked ->
            editor.putBoolean("DailyExpCheck", isChecked)
            editor.apply()
        }
        settingsSwitch2.setOnCheckedChangeListener{_, isChecked ->
            editor.putBoolean("SkipMissingDateCheck", isChecked)
            editor.apply()
        }
    }

}

