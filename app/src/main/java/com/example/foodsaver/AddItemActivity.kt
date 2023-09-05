package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class AddItemActivity : ComponentActivity() {
    //making a var for my button
    private lateinit var addToMainButton: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        addToMainButton = findViewById(R.id.addToMainButton)

        //setting to switch to main on click
        addToMainButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@AddItemActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
