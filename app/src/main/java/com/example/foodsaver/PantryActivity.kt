package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryToMainButton: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantry_layout)
        //Assigning vars
        pantryToMainButton = findViewById(R.id.pantryToMainButton)
        //setting switch activity event
        pantryToMainButton.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent  = Intent(this@PantryActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
