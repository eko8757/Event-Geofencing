package com.dev.eventsgeofencing.geofencing

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.ui.Location
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

@SuppressLint("Registered")
class GeofenceRegistrationService : IntentService(TAG) {

    private lateinit var NAMA_EVENT: String

    companion object {
        private const val TAG = "GeoIntentService"
        private const val CHANNEL_ID = "channel_01"
        private const val GEOFENCE_ID = "Geofence"


        private fun getErrorString(errorCode: Int): String {
            return when (errorCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> "GeoFence not available"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> "Too many GeoFences"
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> "Too many pending intents"
                else -> "Unknown error."
            }
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val name: String = intent.getStringExtra("name")
            NAMA_EVENT = "Kamu berada disekitar event $name"
        } else {
            NAMA_EVENT = getString(R.string.geofence_txt)
        }
        val geofencingEvent = GeofencingEvent.fromIntent(intent)!!
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.errorCode)
        } else {
            val transaction = geofencingEvent.geofenceTransition
            val geofences = geofencingEvent.triggeringGeofences
            val geofence = geofences[0]
            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER && geofence.requestId == GEOFENCE_ID) {
                Log.d(TAG, "You are inside")
            } else {
                Log.d(TAG, "You are outside")
            }
            val geofenceTransitionDetails =
                getGeofenceTrasitionDetails(transaction, geofences)
            sendNotification(geofenceTransitionDetails)
        }
    }

    // Create a detail message with Geofences received
    private fun getGeofenceTrasitionDetails(
        geoFenceTransition: Int,
        triggeringGeofences: List<Geofence>
    ): String {
        val triggeringGeofencesList: ArrayList<String?> = ArrayList()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesList.add(geofence.requestId)
        }
        var status: String? = null
        if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) status =
            "Entering " else if (geoFenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) status =
            "Exiting "
        return status + TextUtils.join(", ", triggeringGeofencesList)
    }

    // Send a notification
    private fun sendNotification(msg: String) {
        Log.i(TAG, "sendNotification: $msg")

        val notificationIntent = Intent()
        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(Location::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val notificationPendingIntent: PendingIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationMng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.enableLights(true)
            mChannel.lightColor = Color.RED
            mChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            mChannel.enableVibration(true)
            mChannel.setShowBadge(true)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            if (notificationMng != null) {
                notificationMng.createNotificationChannel(mChannel)
                notificationMng.notify(0, createNotification(msg, notificationPendingIntent))
            }
        } else {
            if (notificationMng != null) {
                notificationMng.notify(0, createNotification(msg, notificationPendingIntent))
            }
        }
    }

    // Create a notification
    private fun createNotification(
        msg: String,
        notificationPendingIntent: PendingIntent
    ): Notification {
        Log.i(TAG, "createNotification: $msg")
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.drawable.logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
            .setContentTitle(msg)
            .setContentText(NAMA_EVENT)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(notificationPendingIntent)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.RED)
            .setChannelId(CHANNEL_ID)
            .setAutoCancel(true)
        return notificationBuilder.build()
    }

    private fun getTransitionString(transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> "location entered"
            Geofence.GEOFENCE_TRANSITION_EXIT -> "location exited"
            Geofence.GEOFENCE_TRANSITION_DWELL -> "dwell at location"
            else -> "location transition"
        }
    }
}