package com.example.foodsaver

import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class Food() : Serializable {

    //Member Fields, Getters and setters done internally
    var foodItemName = ""

    /*Christian Davis: changed this to public as I need to access it for user input
    if it needs to be private then change it back and I'll think of something
    else*/
    var itemExpirationDate = ""

    //Field to be used with updates
    var daysTillExpiration = 0
    private var itemIsExpired = false
    init {
        if(isValidDate(this.itemExpirationDate)){
            checkExpiration(this)
        }
    }
    //Function to check expiration dates
    //May be updated to include specified windows of time
    fun checkExpiration(foodItem: Food): Boolean {
        val expirationDate = convertExpirationDate(foodItem)


        if (expirationDate.before(Date())) {
            foodItem.itemIsExpired = true
            //Add in Color change
        } else { //
            //Days Till Expiration date to be used for color addition Green/Yellow
            foodItem.daysTillExpiration = calculateTimeBetweenDates(expirationDate)

        }

        return foodItem.itemIsExpired
    }

    //Function Created to calculate the time between dates, may be moved up into
    //checkExpiration function in later changes. Left access public for use outside of class
    fun calculateTimeBetweenDates(expirationDate: Date): Int {


        val timeInMilliseconds = expirationDate.time - Date().time

        var timeInDays = (timeInMilliseconds / 86400000).toInt()

        return timeInDays


    }

    //function added to convert strings into Dates to avoid repetitive code
    fun convertExpirationDate(foodItem: Food): Date {

        val sFormat = SimpleDateFormat("MM-dd-yyyy")

        val formattedExpirationDate = sFormat.parse(foodItem.itemExpirationDate)


        return formattedExpirationDate
    }

    //Function added to check if a string is a valid date
    fun isValidDate(date: String): Boolean {
        val sFormat = SimpleDateFormat("MM-dd-yyyy")
        val shortHandFormat = SimpleDateFormat("MM-dd-yy")

        var isValid = false
        var sDate = Date()
        var convertedDate = date.replace("/", "-")

        //Disallows the user to enter date in any other format than the above
        //sFormat.isLenient = false
        try {
            var dashDate = date.replace('/', '-')
            //Convert shorthand date string to a date and formatting it with full year
            var shortHandDate = shortHandFormat.parse(dashDate)
            var convertedDate = sFormat.format(shortHandDate)
            sDate = sFormat.parse(convertedDate)
            if (sDate.after(Date())) {
                isValid = true
            }

        } catch (e: ParseException) {


        }


        return isValid

    }

    fun convertShortHandYear(date: String): String {

        val sFormat = SimpleDateFormat("MM-dd-yy")
        val Format = SimpleDateFormat("MM-dd-yyyy")
        //Adding in the slash to dash conversion
        var dashDate = date.replace('/', '-')
        //Convert shorthand date string to a date and formatting it with full year
        var shortHandDate = sFormat.parse(dashDate)
        var convertedDate = Format.format(shortHandDate)


        return convertedDate
    }


}