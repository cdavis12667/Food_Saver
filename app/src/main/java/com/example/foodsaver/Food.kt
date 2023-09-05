package com.example.foodsaver

import java.text.SimpleDateFormat
import java.util.Date


class Food() {

    //Member Fields, Getters and setters done internally
    var itemName = ""
    private var expirationDate = ""

    //Field to be used with updates
    //var daysTillExpiration = 0
    private var isExpired = false

    //Function to check expiration dates
    //May be updated to include specified windows of time
    fun CheckExpiration(foodItem: Food): Boolean {
        val Sformat = SimpleDateFormat("MM-dd-yyy")
        val fExpirationDate = Sformat.parse(expirationDate)


        if (fExpirationDate.before(Date())) {
            foodItem.isExpired = true
            //Add in Color change
        } else {
            //Days Till Expiration date to be used for color addition Green/Yellow

        }

        return foodItem.isExpired
    }


}