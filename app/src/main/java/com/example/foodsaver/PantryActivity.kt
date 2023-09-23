package com.example.foodsaver

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity



class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryToMainButton: android.widget.Button
    private lateinit var pantryList: android.widget.ListView

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
        //making an array list
        var arrImageText: ArrayList<ImageTextView> = ArrayList()
        pantryList.adapter = PantryCustomAdapter(this, arrImageText)


        if (GlobalFoodNames.isNotEmpty()) {
            for (food in GlobalFoodNames) {
                if (food.itemExpirationDate == "") {
                    arrImageText.add(ImageTextView(R.drawable.black_circle, food.foodItemName, ""))
                } else {
                    arrImageText.add(MakeImgTxt(food))
                }
            }
        }
        //setting switch activity event
        pantryToMainButton.setOnClickListener {

            val intent = Intent(this@PantryActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
    //this should take in a food object and return a ImageTextView
    private fun MakeImgTxt(food: Food): ImageTextView {
        //We will plug this in later
        val imageID: Int
        //calling this so the class sets days till expiration
        val exp = food.checkExpiration(food)
        //If food is expired set to red
        if (exp) {
            imageID = R.drawable.red_circle
        }
        //If more than 13 days make it green
        else if (food.daysTillExpiration > 13) {
            imageID = R.drawable.green_circle
        }
        //if 13 days or less make it yellow
        else {
            imageID = R.drawable.yellow_circle
        }
        return ImageTextView(imageID, food.foodItemName, food.itemExpirationDate)


    }
}
