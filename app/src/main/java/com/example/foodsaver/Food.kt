package com.example.foodsaver

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Food(itemName: String, expirationDate: String, isExpired: Boolean) {
    var itemName = ""
    var expirationDate = ""
    var isExpired: Boolean = false


    fun CheckExpiration(foodItem: Food): Boolean {





        return foodItem.isExpired
    }


}