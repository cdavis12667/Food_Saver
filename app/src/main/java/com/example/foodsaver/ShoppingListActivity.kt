package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity

class ShoppingListActivity : ComponentActivity() {
    //making vars
    private lateinit var shoppingToMainButton: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_layout)
        //assigning vars
        shoppingToMainButton = findViewById(R.id.shoppingToMainButton)
        shoppingToMainButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(p0: View?) {
                val intent = Intent(this@ShoppingListActivity, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}

