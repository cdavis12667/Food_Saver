package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity



class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryToMainButton: android.widget.Button

    //Making a companion object which is a bit like static objects
    companion object {
        /*This should make it so we put in a string and get out the matching food object
        I'm just setting it as mutable so that we can add more food objects with out running
        into problems
         */
         val GlobalFoodMap = mutableMapOf<String, Food>()

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
