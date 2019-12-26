package com.dev.eventsgeofencing.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.dev.eventsgeofencing.receiver.GeofenceBroadcasrReceiver
import com.dev.eventsgeofencing.receiver.GeofenceErrorMessage
import com.dev.eventsgeofencing.utils.StaticString
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs

class ReminderRepository(private val context: Context) {

    private val gson = Gson()
    private val geofancingClient = LocationServices.getGeofencingClient(context)
    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcasrReceiver::class.java)
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    fun add(reminder: Reminder, success: () -> Unit, failure: (error: String) -> Unit) {
        val geofence = buildGeofence(reminder)
        if (geofence != null && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            geofancingClient.addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent)
                .addOnSuccessListener {
                    saveAll(getAll() + reminder)
                    success()
                }
                .addOnFailureListener {
                    failure(GeofenceErrorMessage.getErrorString(context, it))
                }
        }
    }

    private fun buildGeofence(reminder: Reminder): Geofence? {
        val latitude = reminder.latLng?.latitude
        val longitude = reminder.latLng?.longitude
        val radius = reminder.radius

        if (latitude != null && longitude != null && radius != null) {
            return Geofence.Builder()
                .setRequestId(reminder.id)
                .setCircularRegion(latitude, longitude, radius.toFloat())
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()
        }
        return null
    }

    private fun saveAll(list: List<Reminder>) {
        Prefs.putString(StaticString().REMINDERS, gson.toJson(list))
    }

    fun getAll() : List<Reminder> {
        val strReminders = Prefs.getString(StaticString().REMINDERS, "")
        if (strReminders.contains("REMINDERS")) {
            val remindersString = Prefs.getString(StaticString().REMINDERS, null)
            val arrayOfReminders = gson.fromJson(remindersString, Array<Reminder>::class.java)

            if (arrayOfReminders != null) {
                return arrayOfReminders.toList()
            }
        }
        return listOf()
    }

    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(0)
            .addGeofences(listOf(geofence))
            .build()
    }

    fun get(requestId: String?) = getAll().firstOrNull { it.id == requestId }
    fun getLast() = getAll().lastOrNull()
}