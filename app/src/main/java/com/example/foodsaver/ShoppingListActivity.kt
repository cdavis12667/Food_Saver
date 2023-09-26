package com.example.foodsaver
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames


class ShoppingListActivity : ComponentActivity() {
    //making vars
    private lateinit var shoppingToMainButton: android.widget.Button
    private lateinit var shopText: EditText
    private lateinit var shoppingListAdapter: ArrayAdapter<String>
    private lateinit var addButton: ImageButton
    private lateinit var shopListView: ListView
    private lateinit var adapter: ArrayAdapter<String> // Use ArrayAdapter for simplicity
    private val shoppingItems = mutableListOf<String>()
    private lateinit var searchDialog: AlertDialog
    private lateinit var searchResultsListView: ListView
    private lateinit var shopClear: Button
    private lateinit var searchView: SearchView
    private lateinit var searchListView: ListView
    private lateinit var searchResultsAdapter: ArrayAdapter<String>
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
        shoppingListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        shopListView.adapter = shoppingListAdapter
        shopClear = findViewById<Button>(R.id.shopClear)
        searchView = findViewById(R.id.searchView)
        searchListView = findViewById(R.id.searchListView)
        searchResultsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        searchListView.adapter = searchResultsAdapter
        searchListView.visibility = View.GONE //Invisible until the search starts


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchFood(newText)
                return true

                if (searchView.hasFocus()) {
                    searchResultsListView.visibility = View.VISIBLE
                }
            }
        })

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Search bar lost focus, hide the search results
                searchListView.visibility = View.GONE
            }
        }


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

        // Set an item click listener to toggle strikethrough
        shopListView.setOnItemClickListener { _, view, _, _ ->
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
            shopListView.setOnItemLongClickListener { _, view, position, _ ->
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
        shoppingListAdapter.notifyDataSetChanged()
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
        shoppingListAdapter.notifyDataSetChanged() // Notify the adapter to update the view
    }
}