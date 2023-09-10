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
    var daysTillExpiration:Long = 0
    private var itemIsExpired = false

    //Function to check expiration dates
    //May be updated to include specified windows of time
    fun checkExpiration(foodItem: Food): Boolean {
       val expirationDate= convertExpirationDate(foodItem)


        if (expirationDate.before(Date())) {
            foodItem.itemIsExpired = true
            //Add in Color change
        } else { //
            //Days Till Expiration date to be used for color addition Green/Yellow
            foodItem.daysTillExpiration = calculateTimeBetweenDates(expirationDate )

        }

        return foodItem.itemIsExpired
    }

    //Function Created to calculate the time between dates, may be moved up into
    //checkExpiration function in later changes. Left access public for use outside of class
    fun calculateTimeBetweenDates(expirationDate: Date): Long {

        val timeInMilliseconds = expirationDate.time - Date().time

        return timeInMilliseconds / 86400000


    }

    //function added to convert strings into Dates to avoid repetitive code
    private fun convertExpirationDate(foodItem: Food): Date {
        val sFormat = SimpleDateFormat("MM-dd-yyy")

        val formatedExpirationDate=sFormat.parse(foodItem.itemExpirationDate)

        return formatedExpirationDate
    }


}