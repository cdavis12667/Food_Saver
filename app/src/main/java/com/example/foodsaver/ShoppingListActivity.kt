package com.example.foodsaver

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.activity.ComponentActivity


class ShoppingListActivity : ComponentActivity() {
    //making vars
    private lateinit var shoppingToMainButton: android.widget.Button
    private lateinit var shopText: EditText
    private lateinit var addButton: ImageButton
    private lateinit var shopListView: ListView
    private lateinit var adapter: ArrayAdapter<String> // Use ArrayAdapter for simplicity
    private val shoppingItems = mutableListOf<String>()
    private lateinit var foodList: List<Food>
    private lateinit var shopSearch: ImageButton
    private lateinit var searchDialog: AlertDialog
    private lateinit var searchResultsListView: ListView
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

        shopSearch.setOnClickListener {
            showSearchDialog()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shoppingItems)
        shopListView.adapter = adapter


        shopText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                clearEditText()
            }
        }

        addButton.setOnClickListener {
            addItem()
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
}

