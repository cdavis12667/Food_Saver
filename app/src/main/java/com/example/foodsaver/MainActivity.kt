package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
class MainActivity : ComponentActivity() {
    //Here I'm just making some vars that will be used for setting buttons
    private lateinit var mainToAddButton: android.widget.Button
    private lateinit var mainToPantryButton: android.widget.Button
    private lateinit var mainToShoppingButton: android.widget.Button
    private lateinit var mainToSettingsButton: android.widget.Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        /*I'm now just setting my vars to be equal to the button elements
        I'm also just going to find them by id*/

        mainToAddButton = findViewById(R.id.mainToAddButton)
        mainToPantryButton = findViewById(R.id.mainToPantryButton)
        mainToShoppingButton = findViewById(R.id.mainToShoppingButton)
        mainToSettingsButton = findViewById(R.id.mainToSettingsButton)

        //Going to make some on click listeners which just go off when the user clicks a button

        mainToAddButton.setOnClickListener{
                /*Alright so this class just needs you to override the on click method
                I'm just going to make it switch screens using an intent*/
                val intent = Intent(this@MainActivity, AddItemActivity::class.java)
                startActivity(intent)
        }
        //Next I'm just going to copy what I did above for the rest of the screens
        mainToPantryButton.setOnClickListener{
                val intent = Intent(this@MainActivity, PantryActivity::class.java)
                startActivity(intent)

        }

        mainToShoppingButton.setOnClickListener{
                val intent = Intent(this@MainActivity, ShoppingListActivity::class.java)
                startActivity(intent)
            }

        mainToSettingsButton.setOnClickListener{
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
    }
}
