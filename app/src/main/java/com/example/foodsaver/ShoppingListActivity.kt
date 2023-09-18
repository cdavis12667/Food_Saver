package com.example.foodsaver

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.activity.ComponentActivity


class ShoppingListActivity : ComponentActivity() {
    //making vars
    private lateinit var shoppingToMainButton: android.widget.Button
    private lateinit var shopText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var shopListView: ListView
    private lateinit var adapter: ArrayAdapter<String> // Use ArrayAdapter for simplicity
    private val shoppingItems = mutableListOf<String>()
    private lateinit var shopSearch: ImageButton
    private lateinit var searchDialog: AlertDialog
    private lateinit var searchResultsListView: ListView
    private lateinit var shopClear: Button
    private lateinit var items: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shopping_list_layout)
        //assigning vars
        shoppingToMainButton = findViewById(R.id.shoppingToMainButton)
        shoppingToMainButton.setOnClickListener{
                val intent = Intent(this@ShoppingListActivity, MainActivity::class.java)
                startActivity(intent)
        }
        shopText = findViewById(R.id.shopText)
        addButton = findViewById(R.id.addButton)
        shopListView = findViewById(R.id.shopListView)
        shopSearch = findViewById(R.id.shopSearch)
        shopClear = findViewById<Button>(R.id.shopClear)

        shopSearch.setOnClickListener {
            showSearchDialog()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingItems)


        shopText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                clearEditText()
            }
        }

        addButton.setOnClickListener {
            addItem()
        }
        shopClear.setOnClickListener {
            showClearConfirmationDialog()
        }

        shopListView = findViewById(R.id.shopListView)
        shopListView.adapter = adapter

        // Set an item click listener to toggle strikethrough
        shopListView.setOnItemClickListener { parent, view, position, id ->
            // Get the TextView inside the clicked item
            val textView = view as TextView

            // Toggle strikethrough style
            if (textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG == 0) {
                // Apply strikethrough style
                textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Remove strikethrough style
                textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
            shopListView.setOnItemLongClickListener { parent, view, position, id ->
                showDeleteConfirmationDialog(position)
                true // Return true to indicate that the long-press event is consumed
            }
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
    private fun showSearchDialog(){ //Still requires something to function as intended.
        val dialogView = LayoutInflater.from(this).inflate(R.layout.search_dialog, null)
        val searchText = dialogView.findViewById<EditText>(R.id.searchEditText)
        searchResultsListView = dialogView.findViewById<ListView>(R.id.searchResultsListView)

        searchDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Search") { _, _ ->
                val query = searchText.text.toString().trim()
                searchFood(query)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        searchDialog.show()

    }
    private fun searchFood(query: String) {
        val hardcodedData = listOf("Apple", "Banana", "Orange") // Replace with your actual food names
        val searchResults = hardcodedData.filter { food ->
            food.contains(query, ignoreCase = true)
        }
        /*val searchResults = GlobalFoodNames.filter { food ->
            food.foodItemName.contains(query, ignoreCase = true)
        }*/
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, searchResults)
        searchResultsListView.adapter = adapter

        adapter.notifyDataSetChanged()
    }
    private fun showDeleteConfirmationDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton("Delete") { dialog, which ->
                deleteItem(position)
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
            .setPositiveButton("Clear") { dialog, which ->
                clearShoppingList()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
        private fun clearShoppingList() {
            shoppingItems.clear() // Clear the list of items
            adapter.notifyDataSetChanged() // Notify the adapter to update the view
        }
}

