package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import java.util.Vector


class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryToMainButton: android.widget.Button

    //Making a companion object which is a bit like static objects
    companion object {
         val GlobalFoodNames = Vector<Food>()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantry_layout)
        //Assigning vars
        pantryToMainButton = findViewById(R.id.pantryToMainButton)
        //setting switch activity event
        pantryToMainButton.setOnClickListener {

            val intent = Intent(this@PantryActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
