package com.example.foodsaver

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
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
import java.util.Calendar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    //Here I'm just making some vars that will be used for setting buttons
    private lateinit var mainToAddButton: android.widget.Button
    private lateinit var mainToPantryButton: android.widget.Button
    private lateinit var mainToShoppingButton: android.widget.Button
    private lateinit var mainToSettingsButton: android.widget.Button
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
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


        //Setting up notification receiver intent
        val filter = IntentFilter("pantry_update") // Match the action
        val receiver = NotificationReceiver()
        registerReceiver(receiver, filter)

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
        //Notification permission check, then test notification
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

        }

        //Read preferences and decide scheduling
        val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
        val notificationFrequency = sharedPrefs.getInt("Notification Frequency", 5)
        //setting default for preference, helps with testing and other functions
        //5 is the default;
        if(notificationFrequency == 5){
            // TODO: Separate alert dialog for alerting the user about the default setting.
            //After dialog, they can say okay or go to the Settings screen to set the frequency settings
        }

        //Set up notification timing through AlarmManager (and maybe Broadcast)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver()::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        //Handling the timing
        val intervalMillis = 7 * 24 * 60 * 60 * 1000
        val intervalMonthMillis = 30 * 24 * 60 * 60 * 1000 //30 days in milliseconds

// Set the time you want the notification to appear
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)

// Schedule the notification
        when(notificationFrequency)
        {
            //Daily
            1 -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent)
            }
            //Weekly
            2 -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intervalMillis.toLong(),
                    pendingIntent)
            }
            //Bi-Weekly
            3 -> {
                val intervalMillis = intervalMillis * 2
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intervalMillis.toLong(),
                    pendingIntent)
            }
            //Monthly
            4 -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intervalMonthMillis.toLong(),
                    pendingIntent)
            }
        }

    }

    inner class NotificationReceiver : BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            //Get file, update GlobalFoodNames (just in case MainActivity onCreate isn't called)
            val file = File(filesDir, "Fooddata")
            var expCount = 0
            var expSoonCount = 0
            var expWeekCount = 0
            var noDateCount = 0
            val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
            val notificationFrequency = sharedPrefs.getInt("Notification Frequency", 1)

            if(file.exists())
            {
                GlobalFoodNames = getFoodFile()!!
            }

            if(GlobalFoodNames.isNotEmpty())
            {
                for (food in GlobalFoodNames) {
                    if (food.itemExpirationDate == "") {
                        noDateCount++
                    }
                    else if (food.checkExpiration(food))
                    {
                        expCount++
                    }
                    else if (food.daysTillExpiration in 3..8)
                    {
                        expWeekCount++
                    }
                    else if (food.daysTillExpiration < 3)
                    {
                        expSoonCount++
                    }
                }
            }
            //Make the notification text

            val notificationText = "Expiring within a week: $expWeekCount\n" +
                                    "Expiring within 3 days: $expSoonCount\n" +
                                    "Expired: $expCount\n" +
                                    "Have no expiration date: $noDateCount\n" +
                                    "Tap here to check your pantry."



            //Notification on-tap action; intent and pending intent
            val notificationIntent = Intent(this@MainActivity, PantryActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                this@MainActivity,
                1,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            //Notification builder
            val notificationManager = NotificationManagerCompat.from(this@MainActivity)
            val builder = NotificationCompat.Builder(this@MainActivity, "pantry_notify")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Pantry Update")
                .setContentText("Check the status of your pantry")
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText)) //Later: output all lists */
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission request done in MainActivity
                return
            }
            if(notificationFrequency != 0)
            {
                notificationManager.notify(1,builder.build())
            }
        }
    }
    //Should be defunct after receiver is fully setup; still here for testing.
    fun showNotification(){
        //Get file, update GlobalFoodNames (just in case MainActivity onCreate isn't called)
        val file = File(filesDir, "Fooddata")
        val expList = mutableListOf<String>()
        val expSoonList = mutableListOf<String>()
        val expWeekList = mutableListOf<String>()
        val expDateMissing = mutableListOf<String>()
        val sharedPrefs = getSharedPreferences("FoodSaverPref", Context.MODE_PRIVATE)
        val notificationFrequency = sharedPrefs.getInt("Notification Frequency", 1)

        /*if(file.exists())
        {
            GlobalFoodNames = getFoodFile()!!
        }*/

        if(GlobalFoodNames.isNotEmpty())
        {
            for (food in GlobalFoodNames) {
                if (food.itemExpirationDate == "") {
                    expDateMissing.add(food.foodItemName)
                }
                else if (food.checkExpiration(food))
                {
                    expList.add(food.foodItemName)
                }
                else if (food.daysTillExpiration in 4..7)
                {
                    expWeekList.add(food.foodItemName)
                }
                else if (food.daysTillExpiration in 1 .. 3)
                {
                    expSoonList.add(food.foodItemName)
                }
            }
        }


        //Notification on-tap action; intent and pending intent
        val notificationIntent = Intent(this@MainActivity, PantryActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this@MainActivity,
            1,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        //Notification builder
        val builder = NotificationCompat.Builder(this, "pantry_notify")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Pantry Update")
            .setContentText("Food that requires your attention:")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Testing")) //Later: attnList.joinToString { "\n" } */
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission request done in MainActivity
            return
        }
        if(notificationFrequency != 0)
        {
            notificationManager.notify(1,builder.build())
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
