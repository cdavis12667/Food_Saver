package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames

class AddItemActivity : ComponentActivity() {
    //making a var for my buttons
    private lateinit var addToMainButton: android.widget.Button
    private lateinit var addConfirmB: android.widget.Button
    private lateinit var addFoodNameEntry: android.widget.EditText
    private lateinit var addDateInput: android.widget.EditText
    private lateinit var addDisplaySelectionText: android.widget.TextView
    private lateinit var foodInput: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        addToMainButton = findViewById(R.id.addToMainButton)
        addConfirmB = findViewById(R.id.addConfirmB)
        addFoodNameEntry = findViewById(R.id.addFoodNameEntry)
        addDateInput = findViewById(R.id.addDateInput)
        addDisplaySelectionText = findViewById(R.id.addDisplaySelectionText)


        //setting to switch to main on click
        addToMainButton.setOnClickListener{
                val intent = Intent(this@AddItemActivity, MainActivity::class.java)
                startActivity(intent)
        }


        //Making this on click to take user input and make a food item
        addConfirmB.setOnClickListener {
            foodInput = Food()
            //if name and date are present then make add food name to vector and date to map
            if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isNotEmpty())) {
                foodInput.itemName = addFoodNameEntry.text.toString()
                foodInput.expirationDate = addDateInput.text.toString()
                GlobalFoodNames.add(foodInput)

            }
            //If text is not empty but date is then just enter text and no date
            else if ((addFoodNameEntry.text.isNotEmpty()) &&(addDateInput.text.isEmpty()))
            {
                //Add name to vector to later be used with map in case they decide to add a date
                foodInput.itemName = addFoodNameEntry.text.toString()
                GlobalFoodNames.add(foodInput)

            }
            //if text is empty show error message
            else
            {
                val toast = Toast.makeText(this, "Input Food name", Toast.LENGTH_SHORT)
                toast.show()
            }
            //Going to test that everything is correct using logcat uncomment test code if needed
            //This should print the name of the string to logcat
            //Log.i("Test", GlobalFoodNames.lastElement())
            //Next let's test and see if I can pull a date from the map (It works!)
            //GlobalFoodMap.get(addFoodNameEntry.text.toString())?.let { it1 -> Log.i("Test", it1) }

            addDisplaySelectionText.text = ""
            //Going to display all items in the pantry
            for(food in GlobalFoodNames)
            {

                addDisplaySelectionText.append(food.itemName)
                addDisplaySelectionText.append("\n")
                //feed the key to the map if the map returns a value then print that on the same line
            }

        }

    }
}
