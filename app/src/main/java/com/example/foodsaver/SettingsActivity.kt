package com.example.foodsaver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
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
    private lateinit var spinnerDayOfWeek: Spinner
    private lateinit var settingsSwitch1: Switch
    private lateinit var settingsSwitch2: Switch
    private lateinit var notifyTestButton: Button
    private lateinit var notifyTestButton2: Button
    private lateinit var setting_home:android.widget.ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_layout)
        //shared preferences setup
        val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val radioButtonSelection = sharedPrefs.getInt("FreqButton", R.id.settingsRadioB1)
        val notFreq = sharedPrefs.getInt("NotificationFrequency", 1)
        val expDailyCheck = sharedPrefs.getBoolean("DailyExpCheck", true)
        val missingDateNotify = sharedPrefs.getBoolean("SkipMissingDateCheck", false)
        var setDay: Int
        val daysOfWeek = arrayOf("Sunday","Monday", "Tuesday", "Wednesday","Thursday","Friday","Saturday")

        //assigning vars
        radioGroup = findViewById(R.id.radioGroup)
        settingsRadioB1 = findViewById(R.id.settingsRadioB1)
        settingsRadioB2 = findViewById(R.id.settingsRadioB2)
        settingsRadioB3 = findViewById(R.id.settingsRadioB3)
        settingsRadioB4 = findViewById(R.id.settingsRadioB4)
        settingsRadioB5 = findViewById(R.id.settingsRadioB5)
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek)
        settingsSwitch1 = findViewById(R.id.settingsSwitch1)
        settingsSwitch2 = findViewById(R.id.settingsSwitch2)
        notifyTestButton = findViewById(R.id.notifyTestButton)
        notifyTestButton2 = findViewById(R.id.notifyTestButton2)

        setting_home = findViewById(R.id.setting_home)
        //making event to switch activity
        setting_home.setOnClickListener{
                val intent = Intent(this@SettingsActivity, MainActivity::class.java)
                startActivity(intent)
            }
        //load user selection or defaults
        setDay = sharedPrefs.getInt("NotificationDay", 5)
        radioGroup.check(radioButtonSelection)
        settingsSwitch1.isChecked = expDailyCheck
        settingsSwitch2.isChecked = missingDateNotify

        //Set up spinner adapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daysOfWeek)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerDayOfWeek.adapter = adapter
        spinnerDayOfWeek.setSelection(setDay)



        //listener for radio button, setting notification preferences
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                //Daily
                R.id.settingsRadioB1 -> {
                editor.putInt("NotificationFrequency", 1)
                }
                //Weekly
                R.id.settingsRadioB2 -> {
                    editor.putInt("NotificationFrequency", 2)
                }
                //Bi-Weekly
                R.id.settingsRadioB3 -> {
                    editor.putInt("NotificationFrequency", 3)
                }
                //Monthly
                R.id.settingsRadioB4 -> {
                    editor.putInt("NotificationFrequency", 4)
                }
                //Off
                R.id.settingsRadioB5 -> {
                    editor.putInt("NotificationFrequency", 0)
                }
            }
            editor.putInt("FreqButton", checkedId)
            editor.apply()
        }
        //listener for setting notification day of the week
        spinnerDayOfWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                editor.putInt("NotificationDay", p2)
                editor.apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
                //Unneeded? Default will be Friday
            }
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
        //Test expiration check notification
        notifyTestButton2.setOnClickListener {
            val intent = Intent("daily_expire_check")
            //Send the broadcast to Main
            if(sharedPrefs.getBoolean("DailyExpCheck", false))
            {
                editor.putBoolean("DailyExpCheck", true)
                sendBroadcast(intent)
                editor.putBoolean("DailyExpCheck", false)

            }
            else
            {
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

