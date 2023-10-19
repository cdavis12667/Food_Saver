package com.example.foodsaver
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames
import java.io.EOFException
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


class ShoppingListActivity : ComponentActivity() {
    //making vars
    private lateinit var shopText: EditText
    private lateinit var shoppingListAdapter: ArrayAdapter<String>
    private lateinit var addButton: ImageButton
    private lateinit var shopListView: ListView
    private lateinit var adapter: ArrayAdapter<String> // Use ArrayAdapter for simplicity
    private var shoppingItems = mutableListOf<String>()
    //private lateinit var searchDialog: AlertDialog
    private lateinit var searchResultsListView: ListView
    private lateinit var shopClear: Button
    private lateinit var searchView: SearchView
    private lateinit var searchListView: ListView
    private lateinit var searchResultsAdapter: ArrayAdapter<String>
    private lateinit var exportShopList: ImageButton
    private lateinit var shoppinglist_home: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_layout)
        //assigning vars
        shoppinglist_home = findViewById(R.id.shoppinglist_home)
        shoppinglist_home.setOnClickListener{
            val intent = Intent(this@ShoppingListActivity, MainActivity::class.java)
            startActivity(intent)
        }
        shopText = findViewById(R.id.shopText)
        addButton = findViewById(R.id.addButton)
        shopListView = findViewById(R.id.shopListView)
        shoppingListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        shopListView.adapter = shoppingListAdapter
        shopClear = findViewById(R.id.shopClear)
        searchView = findViewById(R.id.searchView)
        searchListView = findViewById(R.id.searchListView)
        searchResultsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        searchListView.adapter = searchResultsAdapter
        searchListView.visibility = View.GONE //Invisible until the search starts
        exportShopList = findViewById(R.id.exportShopList)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingItems)
        shopListView = findViewById(R.id.shopListView)
        shopListView.adapter = adapter

        val file = File(filesDir, "Fooddata")

        if(file.exists())
        {
            GlobalFoodNames = getFoodFile()!!
        }
        //making a file for food data
        val shoppingFile = File(filesDir, "Shoppingdata")
        //checking if fooddata already exists
        if(shoppingFile.exists()){
            //if it does clear shopping items
            shoppingItems.clear()
            //get data from file and add to shopping items
            shoppingItems.addAll(getShoppingItems()!!)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchFood(newText)
                return true

                if (searchView.hasFocus()) { //This makes the search results listview only visible when the search view has focus
                    searchResultsListView.visibility = View.VISIBLE
                }
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // If the search bar loses focus, the search results get hidden
                searchListView.visibility = View.GONE
            }
        }

        //Deletes ONLY the tooltip when gaining focus
        shopText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            val starttext = shopText.text.toString().trim()
            if (hasFocus) {
                if(starttext == "Type in an item...")
                {
                    clearEditText()
                }
            }
        }
        exportShopList.setOnClickListener{
           showExportConfirmationDialog()
        }

        addButton.setOnClickListener {
            addItem()
        }
        shopClear.setOnClickListener {
            showClearConfirmationDialog()
        }

        // Set an item click listener to toggle strikethrough
        shopListView.setOnItemClickListener { _, _, number, _ ->
            // Get the TextView inside the clicked item
            //val textView = view as TextView

            // Check for checkmark
            if (!shoppingItems[number].endsWith("\u2713")) {
                //If check mark is not thier than add it
                // Apply strikethrough style
                shoppingItems[number] = shoppingItems[number] +" "+"\u2713"

            } else {
                Log.d("String Debug", shoppingItems[number].dropLast( shoppingItems[number].length))
                // Remove checkmark
                shoppingItems[number] = shoppingItems[number].removeRange(shoppingItems[number].length - 1, shoppingItems[number].length)

            }
            shopListView.setOnItemLongClickListener { _, _, position, _ ->
                showDeleteConfirmationDialog(position)
                true
            }
            adapter.notifyDataSetChanged()
        }
    }
    private fun addItem() {
        val itemName = shopText.text.toString().trim()
        if (itemName.isNotEmpty()) {
            shoppingItems.add(itemName)
            adapter.notifyDataSetChanged()
            shopText.text.clear()
        }
    }
    private fun clearEditText() {
        shopText.text.clear()
    }

    private fun searchFood(query: String) {
        val searchResults = GlobalFoodNames.filter { food ->
            food.foodItemName.contains(query, ignoreCase = true)
        }

        val searchResNames = searchResults.map {it.foodItemName}
        searchResultsAdapter.clear() // Clear the adapter's current data
        searchResultsAdapter.addAll(searchResNames) // Add the filtered results to the adapter
        searchResultsAdapter.notifyDataSetChanged()

        if (searchResults.isNotEmpty()){
            searchListView.visibility = View.VISIBLE
        }
        else {
            searchListView.visibility = View.GONE
        }
        searchListView.setOnItemClickListener { _, _, position, _ ->
            // Get the selected item from search results
            val selectedItem = searchResNames[position]

            // Add the selected item to your shopping list (items)
            shoppingItems.add(selectedItem)
            shoppingListAdapter.notifyDataSetChanged()

            // Clear the search query and hide the search results
            searchView.setQuery("", false) // Clear query
            searchView.clearFocus() // Remove focus from search bar
            searchResultsListView.visibility = View.GONE
        }
    }
    /*this takes all items with a check mark in the shopping list and
    exports them to the global food list*/
    private fun addShoppingToPantry(){
        //temp list
        val templist = mutableListOf<String>()
        templist.addAll(shoppingItems)
        //loop through list

        for(name in shoppingItems){
            //check for check mark
            if(name.contains("\u2713")){
                //make food object with that name
                val food = Food()
                //set food name without checkmark
                food.foodItemName = name.removeRange(name.length - 1, name.length)
                //add that name to the global food list
                GlobalFoodNames.add(food)
                //remove item from shopping list
                templist.remove(name)
            }
        }
        shoppingItems.clear()
        shoppingItems.addAll(templist)
        //update adapter
        adapter.notifyDataSetChanged()
        //resort global
        GlobalFoodNames.sortBy { it.foodItemName }
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { _, _ ->
                deleteItem(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    //Setting confirm dialog for export
    private fun showExportConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Export")
            .setMessage("Move checked items to pantry and remove from list?")
            .setPositiveButton("Export") { _, _ ->
                addShoppingToPantry()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun deleteItem(position: Int) {
        shoppingItems.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun showClearConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Clear Shopping List")
            .setMessage("Are you sure you want to clear the shopping list?")
            .setPositiveButton("Clear") { _, _ ->
                clearShoppingList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun clearShoppingList() {
        shoppingItems.clear() // Clear the list of items
        adapter.notifyDataSetChanged() // Notify the adapter to update the view
    }
    //Method to pull food file without requiring the pantry to be opened first
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
    //Functions for saving and writing
    //This functions saves a list and returns a bool if it worked or did not work
    private fun saveShoppingItems(listSave: MutableList<String>): Boolean{
        try {
            val fos = openFileOutput("Shoppingdata", MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(listSave)
            oos.close()
        }
        catch(e: IOException){
            e.printStackTrace()
            return false
        }
        return true
    }
    //this function reads an object and returns null if no object is their
    private fun getShoppingItems(): MutableList<String>? {
        try {

            val fis = openFileInput("Shoppingdata")
            val ois = ObjectInputStream(fis)
            val listSave = ois.readObject()
            ois.close()
            if (listSave != null) {
                @Suppress("UNCHECKED_CAST")
                return listSave as MutableList<String>
            }
        } catch (e: EOFException) {
            e.printStackTrace()
        }
        return null
    }
    override fun onStop() {
        super.onStop()
        saveShoppingItems(shoppingItems)
        saveFood(GlobalFoodNames)
    }
    override fun onPause() {
        super.onPause()
        saveShoppingItems(shoppingItems)
        saveFood(GlobalFoodNames)
    }
}