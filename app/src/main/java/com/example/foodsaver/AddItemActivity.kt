package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames



class AddItemActivity : ComponentActivity() {
    //making a var for my buttons
    private lateinit var addToMainButton: android.widget.Button
    private lateinit var addConfirmB: android.widget.Button
    private lateinit var addFoodNameEntry: android.widget.EditText
    private lateinit var addDateInput: android.widget.EditText
    private lateinit var foodInput: Food
    private lateinit var addList: android.widget.ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var addEditButton: android.widget.Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        addToMainButton = findViewById(R.id.addToMainButton)
        addConfirmB = findViewById(R.id.addConfirmB)
        addFoodNameEntry = findViewById(R.id.addFoodNameEntry)
        addDateInput = findViewById(R.id.addDateInput)
        addList = findViewById(R.id.addList)
        addEditButton = findViewById(R.id.addEditButton)
        //I just need something to hold the selected index from addList
        var indexHolder = 0
        //Setting my adapter to the simple layout
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,)
        //setting my lists views adapter to be adapter
        addList.adapter = adapter
        //We need this here other wise the activity switching wipes the list
        if (GlobalFoodNames.isNotEmpty())
        {
            for(food in GlobalFoodNames)
            {
                //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
        }



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
                foodInput.foodItemName = addFoodNameEntry.text.toString()
                foodInput.itemExpirationDate = addDateInput.text.toString()
                GlobalFoodNames.add(foodInput)
                /*In order to replaces the slashes syntax would be
                * Stringname.replace('/','-') It will ignore the
                * name if it is already correct*/
            }
            //If text is not empty but date is then just enter text and no date
            else if ((addFoodNameEntry.text.isNotEmpty()) &&(addDateInput.text.isEmpty()))
            {
                //Add name to list to later be used with map in case they decide to add a date
                foodInput.foodItemName = addFoodNameEntry.text.toString()
                GlobalFoodNames.add(foodInput)

            }
            //if text is empty show error message
            else
            {
                val toast = Toast.makeText(this, "Input Food name", Toast.LENGTH_SHORT)
                toast.show()
            }

            //Sort
            GlobalFoodNames.sortBy { it.foodItemName }
            //Clear List
            adapter.clear()
            //Going to display all items in the pantry
            for(food in GlobalFoodNames)
            {
               //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }

        }
        //this is a listener that fires of when an item in the list is clicked
        addList.setOnItemClickListener { adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            //When an entry is clicked print that entry to the text boxes
            addFoodNameEntry.text.clear()
            addFoodNameEntry.text.append(GlobalFoodNames[i].foodItemName)
            addDateInput.text.clear()
            addDateInput.text.append(GlobalFoodNames[i].itemExpirationDate)
            //We need to save the index for later when we change the entry
            indexHolder = i
        }
        /*Now I need to make a button that takes the text box text and uses it to replace the entry

         */
        addEditButton.setOnClickListener {
            //I need to take whats in the text box and override the global food list
            if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isNotEmpty())) {
                //Change the entry using the index from select list item event
                GlobalFoodNames[indexHolder].foodItemName = addFoodNameEntry.text.toString()
                GlobalFoodNames[indexHolder].itemExpirationDate = addDateInput.text.toString()
                //I need to change adapters entry


            } else if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isEmpty())) {
                //Change the entry using the index from select list item event
                GlobalFoodNames[indexHolder].foodItemName = addFoodNameEntry.text.toString()
            } else {
                //do nothing
            }
            //Resorting the names
            GlobalFoodNames.sortBy { it.foodItemName }
            //Clear and reset adapter
            adapter.clear()
            //Display new name list
            for (food in GlobalFoodNames) {
                //Pull from Global Food Names and display
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
        }

    }
}
