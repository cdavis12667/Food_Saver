package com.example.foodsaver

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames
import java.io.EOFException
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class AddItemActivity : ComponentActivity() {
    //Var creation
    private lateinit var addConfirmB: android.widget.Button
    private lateinit var addFoodNameEntry: android.widget.EditText
    private lateinit var addDateInput: android.widget.EditText
    private lateinit var foodInput: Food
    private lateinit var addList: android.widget.ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var addRemoveButton: android.widget.Button
    private lateinit var edit_home: android.widget.ImageButton
    private lateinit var noDateButton: android.widget.Button
    private lateinit var closeButton: android.widget.Button
    private lateinit var dialog: AlertDialog
    private lateinit var dateView: android.widget.GridView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_item_layout)
        //assigning var
        edit_home = findViewById(R.id.edit_home)
        addConfirmB = findViewById(R.id.addConfirmB)
        addFoodNameEntry = findViewById(R.id.addFoodNameEntry)
        addDateInput = findViewById(R.id.addDateInput)
        addList = findViewById(R.id.addList)
        addRemoveButton = findViewById(R.id.addRemoveButton)
        noDateButton = findViewById(R.id.no_date_Button)





        //Setting my adapter to the simple layout
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        addList.adapter = adapter
        //This will hold the index of which TextView child of the ListView was clicked
        var indexHolder = 0
        //bool to see if the list has been clicked used to switch from edit and add
        var listClickedFlag = false
        //Checks to see if the file is empty if not then sets saved data to global food names
        val file = File(filesDir, "Fooddata")
        if(file.exists())
        {
            //Loads GlobalFoodNames from getFoodFile and then loops and prints to the list
            GlobalFoodNames = getFoodFile()!!
            for (food in GlobalFoodNames) {
                adapter.add(food.foodItemName + " " + food.itemExpirationDate)
            }
        }
        //setting to switch to main on click
        edit_home.setOnClickListener {
            val intent = Intent(this@AddItemActivity, MainActivity::class.java)
            startActivity(intent)
        }
        //Making this on click to take user input and make a food item
        addConfirmB.setOnClickListener {

            foodInput = Food()

            //check to make sure that the text boxes are not empty
            if ((addFoodNameEntry.text.isNotEmpty()) && (addDateInput.text.isNotEmpty())) {

                //check for valid date
                val dateInputText=addDateInput.text.toString().replace("/","-")

                if (foodInput.isValidDate(addDateInput.text.toString())) {
                    foodInput.foodItemName = addFoodNameEntry.text.toString()
                    foodInput.itemExpirationDate = foodInput.convertShortHandYear(dateInputText)
                    //if this is true it means they clicked and want to edit
                    if (listClickedFlag) {
                        GlobalFoodNames[indexHolder].foodItemName = addFoodNameEntry.text.toString()
                        GlobalFoodNames[indexHolder].itemExpirationDate = foodInput.convertShortHandYear(dateInputText)
                    }
                    // if the flag is not true it means they want to add an item
                    else  {
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
                else{
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
        addList.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
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
        //Pop up window for fresh items
        noDateButton.setOnClickListener {

            val inflater = LayoutInflater.from(this)
            val builder = AlertDialog.Builder(this)


            val dateWindow = inflater.inflate(R.layout.fresh_food_popup_layout, null)

            closeButton = dateWindow.findViewById(R.id.close_popup_window)
            dateView = dateWindow.findViewById(R.id.freshDateView)
            //Reading in text file
            val inStream: InputStream = assets.open("freshfooddates.txt")
            val freshFoodDayArray = mutableListOf<String>()
            inStream.bufferedReader().forEachLine { freshFoodDayArray.add(it) }

            // File("freshfooddates.txt").forEachLine { pAdapter.add(it) }
            //adapter for listView

            val pAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
            dateView.adapter = pAdapter

            //Reading array into list View

            for (s in freshFoodDayArray) {
                pAdapter.add(s)
            }




            builder.setView(dateWindow)
            dialog = builder.create()
            dialog.show()

            closeButton.setOnClickListener {
                dialog.dismiss()
            }

        }
    }
    //Functions for saving and writing
    //This functions saves a list and returns a bool if it worked or did not work
    private fun saveFood(mutableFoodList: MutableList<Food>): Boolean{
        try {
            val fos = openFileOutput("Fooddata", MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(mutableFoodList)
            oos.close()
        }
        catch(e: IOException){
            e.printStackTrace()
            return false
        }
        return true
    }
    //this function reads an object and returns null if no object is their
    private fun getFoodFile(): MutableList<Food>? {
        try {

            val fis = openFileInput("Fooddata")
            val ois = ObjectInputStream(fis)
            val foodlist = ois.readObject()
            ois.close()
            if (foodlist != null) {
                return foodlist as MutableList<Food>
            }
        } catch (e: EOFException) {
            e.printStackTrace()
        }
        return null
    }
//Saving before stopping
    override fun onStop() {
        super.onStop()
        saveFood(GlobalFoodNames)
    }
    override fun onPause(){
        super.onPause()
        saveFood(GlobalFoodNames)
    }
}
