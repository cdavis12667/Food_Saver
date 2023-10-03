package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames
import java.text.ParseException


class AddItemActivity : ComponentActivity() {
    //making a var for my buttons
    private lateinit var addToMainButton: android.widget.Button
    private lateinit var addConfirmB: android.widget.Button
    private lateinit var addFoodNameEntry: android.widget.EditText
    private lateinit var addDateInput: android.widget.EditText
    private lateinit var foodInput: Food
    private lateinit var addList: android.widget.ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var addRemoveButton: android.widget.Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        addToMainButton = findViewById(R.id.addToMainButton)
        addConfirmB = findViewById(R.id.addConfirmB)
        addFoodNameEntry = findViewById(R.id.addFoodNameEntry)
        addDateInput = findViewById(R.id.addDateInput)
        addList = findViewById(R.id.addList)
        addRemoveButton = findViewById(R.id.addRemoveButton)
        //I just need something to hold the selected index from addList
        var indexHolder = 0
        var listClickedFlag = false
        //Setting my adapter to the simple layout
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        //setting my lists views adapter to be adapter
        addList.adapter = adapter
        //We need this here other wise the activity switching wipes the list
        if (GlobalFoodNames.isNotEmpty()) {
            for (food in GlobalFoodNames) {
                //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
        }

        //setting to switch to main on click
        addToMainButton.setOnClickListener {
            val intent = Intent(this@AddItemActivity, MainActivity::class.java)
            startActivity(intent)
        }


        //Making this on click to take user input and make a food item
        addConfirmB.setOnClickListener {

            foodInput = Food()

            //check to make sure that the text boxes are not empty
            if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isNotEmpty())) {
                //Make a food object
                //check for valid date
                //var dateInputText = ""

                   // dateInputText = foodInput.convertShortHandYear(addDateInput.text.toString())

                if (foodInput.isValidDate(addDateInput.text.toString())) {
                    foodInput.foodItemName = addFoodNameEntry.text.toString()
                    foodInput.itemExpirationDate = foodInput.convertShortHandYear(addDateInput.text.toString())
                    //if this is true it means they clicked and want to edit
                    if (listClickedFlag) {
                        GlobalFoodNames[indexHolder].foodItemName = addFoodNameEntry.text.toString()
                        GlobalFoodNames[indexHolder].itemExpirationDate = foodInput.convertShortHandYear(addDateInput.text.toString())


                    }
                    // if the flag is not true it means they want to add an item
                    else if (!listClickedFlag) {
                        GlobalFoodNames.add(foodInput)
                    }
                }
                //if date is not valid then show message
                else {
                    val toast =
                        Toast.makeText(this, "Input Valid Date mm-dd-yyyy", Toast.LENGTH_SHORT)
                    toast.show()
                }

            }
            //this should be the same as above but with no date
            else if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isEmpty())) {
                //make a food object
                foodInput.foodItemName = addFoodNameEntry.text.toString()
                //if this is true that means they clicked the list and want to edit
                if (listClickedFlag) {
                    GlobalFoodNames[indexHolder].foodItemName = addFoodNameEntry.text.toString()
                    GlobalFoodNames[indexHolder].itemExpirationDate = ""
                }
                //if not true than just add
                else if (!listClickedFlag) {
                    GlobalFoodNames.add(foodInput)
                }
            }
            //have a check for if nothing is there and show error message
            else {
                val toast = Toast.makeText(this, "Input Food name", Toast.LENGTH_SHORT)
                toast.show()
            }
            //Sort
            GlobalFoodNames.sortBy { it.foodItemName }
            //Clear List
            adapter.clear()
            //Going to display all items in the pantry
            for (food in GlobalFoodNames) {
                //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
            listClickedFlag = false
            addFoodNameEntry.text.clear()
            addDateInput.text.clear()

        }
        //this is a listener that fires off when an item in the list is clicked
        addList.setOnItemClickListener { adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            //When an entry is clicked print that entry to the text boxes
            addFoodNameEntry.text.clear()
            addFoodNameEntry.text.append(GlobalFoodNames[i].foodItemName)
            addDateInput.text.clear()
            addDateInput.text.append(GlobalFoodNames[i].itemExpirationDate)
            //We need to save the index for later when we change the entry
            indexHolder = i
            //this is a flag that sets to see if the user has clicked the list
            listClickedFlag = true
        }

        //Event fires off when the button is clicked
        addRemoveButton.setOnClickListener {
            //We take the index from index holder than just remove it from the global food list
            GlobalFoodNames.removeAt(indexHolder)
            //We should clear the text box
            addFoodNameEntry.text.clear()
            addDateInput.text.clear()
            //Next we need to clear the adapter and rewrite the list
            //Clear and reset adapter
            adapter.clear()
            //Display new name list
            for (food in GlobalFoodNames) {
                //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
            //reset flag to false
            listClickedFlag = false
        }

    }
}
