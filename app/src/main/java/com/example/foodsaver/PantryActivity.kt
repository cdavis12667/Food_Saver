package com.example.foodsaver

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import java.io.EOFException
import java.io.File
import java.io.ObjectInputStream


class PantryActivity : ComponentActivity() {
    //setting vars
    private lateinit var pantryList: android.widget.ListView
    private lateinit var pantrySortButton: Button
    private lateinit var adapter: PantryCustomAdapter
    private lateinit var pantrySharedPreferences: SharedPreferences
    private lateinit var pantryEditor: Editor
    private lateinit var pantry_home:android.widget.ImageButton
    //Making a companion object which is a bit like static objects
    companion object {
         var GlobalFoodNames = mutableListOf<Food>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantry_layout)
        //Assigning vars
        pantry_home = findViewById(R.id.pantry_home)
        pantryList = findViewById(R.id.Pantrylist)
        pantrySortButton = findViewById(R.id.pantrySortButton)
        pantrySharedPreferences = getSharedPreferences("pantrySharedPreferences", Context.MODE_PRIVATE)
        pantryEditor = pantrySharedPreferences.edit()
        //making an array list
        val arrImageText: ArrayList<ImageTextView> = ArrayList()
        //setting the adapter to my custom adapter
        adapter = PantryCustomAdapter(this, arrImageText)
        //setting the pantry list adapter to adapter
        pantryList.adapter = adapter
        //I don't want to modify the global list when I sort so instead I use this
        var sortList = mutableListOf<Food>()
        val file = File(filesDir, "Fooddata")
        if(file.exists())
        {
            //Import global food list and set it to sortList
            sortList = getFoodFile()!!
            //If sort flag is true then sort by name
            if(pantrySharedPreferences.getBoolean("SortFlag", false)){
                sortList.sortBy { it.foodItemName}
                pantrySortButton.text = getString(R.string.sort_by_date)
            }
            //if sort flag is false sort by date
            else{
                sortFoodByDate(sortList)
                pantrySortButton.text = getString(R.string.sort_alphabetically)
            }
            //Set the list attached to the adapter by converting the food list to a imagetext array
            arrImageText.addAll(makeImageTextArr(sortList))
            //update data set
            adapter.notifyDataSetChanged()
        }
        //setting switch activity event
        pantry_home.setOnClickListener {
            val intent = Intent(this@PantryActivity, MainActivity::class.java)
            startActivity(intent)
        }
        //Button for switching sort
        pantrySortButton.setOnClickListener{
            if(pantrySharedPreferences.getBoolean("SortFlag", false)){
                //resort the list and change text
                pantryEditor.putBoolean("SortFlag", false)
                sortFoodByDate(sortList)
                pantrySortButton.text = getString(R.string.sort_alphabetically)
            }
            else{
                //resort the list and change text
                pantryEditor.putBoolean("SortFlag", true)
                sortList.sortBy { it.foodItemName}
                pantrySortButton.text = getString(R.string.sort_by_date)
            }
            //Update pref
            pantryEditor.apply()
            //clear list fill list and notify adapter
            arrImageText.clear()
           arrImageText.addAll(makeImageTextArr(sortList))
            adapter.notifyDataSetChanged()
        }
    }

    //change a list of food items to a list of imagetext items
    private fun makeImageTextArr(foodList: MutableList<Food>): ArrayList<ImageTextView>{
        val returnArr = arrayListOf<ImageTextView>()
        for (food in foodList) {
            if (food.itemExpirationDate == "") {
                returnArr.add(ImageTextView(R.drawable.calender_date, food.foodItemName, ""))
            } else {
                returnArr.add(MakeImgTxt(food))
            }
        }
        return returnArr
    }
    //sort food by days till expiration
    private fun sortFoodByDate(foodList: MutableList<Food>){
        for (food in foodList) {

            if (food.itemExpirationDate != "") {
                food.checkExpiration(food)
            }
            else{
                food.daysTillExpiration = 1000
            }
        }
        foodList.sortBy { it.daysTillExpiration}
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
    private fun getFoodFile(): MutableList<Food>? {
        try {

            val fis = openFileInput("Fooddata")
            val ois = ObjectInputStream(fis)
            val foodlist = ois.readObject()
            ois.close()
            if (foodlist != null) {
                @Suppress("UNCHECKED_CAST")
                return foodlist as MutableList<Food>
            }
        } catch (e: EOFException) {
            e.printStackTrace()
        }
        return null
    }



}

