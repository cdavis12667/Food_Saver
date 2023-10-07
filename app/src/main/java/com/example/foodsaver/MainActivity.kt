package com.example.foodsaver

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.foodsaver.PantryActivity.Companion.GlobalFoodNames
import java.io.EOFException
import java.io.File
import java.io.ObjectInputStream

class MainActivity : ComponentActivity() {
    //Here I'm just making some vars that will be used for setting buttons
    private lateinit var mainToAddButton: android.widget.Button
    private lateinit var mainToPantryButton: android.widget.Button
    private lateinit var mainToShoppingButton: android.widget.Button
    private lateinit var mainToSettingsButton: android.widget.Button
    private var attnList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        /*I'm now just setting my vars to be equal to the button elements
        I'm also just going to find them by id*/

        mainToAddButton = findViewById(R.id.mainToAddButton)
        mainToPantryButton = findViewById(R.id.mainToPantryButton)
        mainToShoppingButton = findViewById(R.id.mainToShoppingButton)
        mainToSettingsButton = findViewById(R.id.mainToSettingsButton)

        //Getting the file. This is to assist the notification channel.
        val file = File(filesDir, "Fooddata")


        //Setting up notification manager

        val notificationId = 1 // Just in case it's needed

        //Going to make some on click listeners which just go off when the user clicks a button

        mainToAddButton.setOnClickListener{
                /*Alright so this class just needs you to override the on click method
                I'm just going to make it switch screens using an intent*/
                val intent = Intent(this@MainActivity, AddItemActivity::class.java)
                startActivity(intent)
        }
        //Next I'm just going to copy what I did above for the rest of the screens
        mainToPantryButton.setOnClickListener{
                val intent = Intent(this@MainActivity, PantryActivity::class.java)
                startActivity(intent)

        }

        mainToShoppingButton.setOnClickListener{
                val intent = Intent(this@MainActivity, ShoppingListActivity::class.java)
                startActivity(intent)
            }

        mainToSettingsButton.setOnClickListener{
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }

        //Retrieve file and update GlobalFoodNames *May not be required in other activities anymore!*
        if(file.exists())
        {
            GlobalFoodNames = getFoodFile()!!
        }

        if(GlobalFoodNames.isNotEmpty())
        {
            for (food in GlobalFoodNames) {
                if (food.itemExpirationDate == "" || food.daysTillExpiration <= 7){
                    attnList.add(food.foodItemName)
                }
            }
        }

        //Notification builder
        val builder = NotificationCompat.Builder(this, "pantry_notify")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pantry Update")
            .setContentText("Food that requires your attention:")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Testing")) //Later: attnList.joinToString { "\n" } */
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        //Check for build version before establishing notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "pantry_notify"
            val channelName = "Pantry Notification"
            val channelDescription = "A list of items that may require the user's attention."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)


        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
            val notificationPermissionAlertDialog = AlertDialog.Builder(this)
            notificationPermissionAlertDialog.setTitle("Permissions Needed")
            notificationPermissionAlertDialog.setMessage("Permissions for notifications have not been enabled. You can enable them in settings now or choose continue without notifications.")
            notificationPermissionAlertDialog.setPositiveButton("Set Permissions", DialogInterface.OnClickListener{notificationPermissionAlertDialog, which ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            })
            notificationPermissionAlertDialog.setNegativeButton("Ignore", DialogInterface.OnClickListener{notificationPermissionAlertDialog, which ->
                notificationPermissionAlertDialog.dismiss()
            })
            notificationPermissionAlertDialog.show()
        } else {
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(notificationId, builder.build())
        }





    }
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            val confirmNoPermission = AlertDialog.Builder(this)
            confirmNoPermission.setTitle("Permissions Not Granted")
            confirmNoPermission.setMessage("Permissions for notifications will stay disabled. As a result, some notifications will be disabled.")
            confirmNoPermission.setNegativeButton("Dismiss", DialogInterface.OnClickListener{confirmNoPermission, which ->
                confirmNoPermission.dismiss()
            })
        }
    }

}
