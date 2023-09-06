package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodMap

class AddItemActivity : ComponentActivity() {
    //making a var for my button
    private lateinit var addToMainButton: android.widget.Button
    private lateinit var addConfirmB: android.widget.Button
    private lateinit var addFoodNameEntry: android.widget.EditText
    private lateinit var addDateInput: android.widget.EditText
    private lateinit var foodInput: Food

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        addToMainButton = findViewById(R.id.addToMainButton)
        addConfirmB = findViewById(R.id.addConfirmB)
        addFoodNameEntry = findViewById(R.id.addFoodNameEntry)
        addDateInput = findViewById(R.id.addDateInput)
        foodInput = Food()
        //setting to switch to main on click
        addToMainButton.setOnClickListener{
                val intent = Intent(this@AddItemActivity, MainActivity::class.java)
                startActivity(intent)
        }
        //Making this on click to take user input and make a food item
        addConfirmB.setOnClickListener {

            //if name and date are present then make a food object like normal
            if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isNotEmpty())) {
                //This is all in progress and needs to be tested using breakpoints
                //This should set the item name to the string entry
                foodInput.itemName = addFoodNameEntry.toString()
                //This should set foods date
                foodInput.expirationDate = addDateInput.toString()
                //adding this to the global var
                GlobalFoodMap.put(addFoodNameEntry.toString(), foodInput)
                /*I'm worried that food input is just going to get overwritten on the next use but
                maybe I'm just tired and at least it's easy to test
                 */

            }
            //If text is not empty but date is then just enter text and no date
            else if ((addFoodNameEntry.text.isNotEmpty()) &&(addDateInput.text.isEmpty()))
            {
                foodInput.itemName = addFoodNameEntry.text.toString()
                GlobalFoodMap.put(addFoodNameEntry.toString(), foodInput)
                Log.i("info", "Else if hits")
                Log.i("info", "Testing text " + addFoodNameEntry.text.toString())

            }
            //if text is empty show error message
            else
            {
                val toast = Toast.makeText(this, "Input name", Toast.LENGTH_SHORT)
                toast.show()
            }
            //Using this for testing
            /*The below code returns null pointer and crashes the app
            for what ever reason
             */
            //Log.i("info", "Testing if the map has an entry" + PantryActivity.GlobalFoodMap.get("Berry").itemName)
        }

    }
}
