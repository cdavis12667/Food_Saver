package com.example.foodsaver

import java.text.SimpleDateFormat
import java.util.Date


class Food() {

    //Member Fields, Getters and setters done internally
    var foodItemName = ""
    /*Christian Davis: changed this to public as I need to access it for user input
    if it needs to be private then change it back and I'll think of something
    else*/
    var itemExpirationDate = ""

    //Field to be used with updates
    //var daysTillExpiration = 0
    private var itemIsExpired = false

    //Function to check expiration dates
    //May be updated to include specified windows of time
    fun checkExpiration(foodItem: Food): Boolean {
        val sFormat = SimpleDateFormat("MM-dd-yyy")
        val formatExpirationDate = sFormat.parse(itemExpirationDate)


        if (formatExpirationDate.before(Date())) {
            foodItem.itemIsExpired = true
            //Add in Color change
        } else { //
            //Days Till Expiration date to be used for color addition Green/Yellow

        }

        return foodItem.itemIsExpired
    }


}