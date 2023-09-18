package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.ComponentActivity



class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryToMainButton: android.widget.Button
    private lateinit var pantryList: android.widget.ListView
    private lateinit var pantryAdapter: ArrayAdapter<String>
    private lateinit var pantryImageAdapter: ArrayAdapter<ImageView>


    //Making a companion object which is a bit like static objects
    companion object {
         var GlobalFoodNames = mutableListOf<Food>()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantry_layout)
        //Assigning vars
        pantryToMainButton = findViewById(R.id.pantryToMainButton)
        pantryList = findViewById(R.id.Pantrylist)
        //setting adapter
        pantryAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        pantryImageAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        //Setting pantry lists adapter to be pantryAdapter
        pantryList.adapter = pantryAdapter
        //Check that global food list is not empty if not then write it to the adapter
        if (GlobalFoodNames.isNotEmpty())
        {
            for(food in GlobalFoodNames)
            {
                //Pull from Global Food Names and display
                pantryAdapter.add(food.foodItemName + "\n" + food.itemExpirationDate)
            }
        }
        //setting switch activity event
        pantryToMainButton.setOnClickListener {

            val intent = Intent(this@PantryActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
