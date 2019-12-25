package com.dev.eventsgeofencing.receiver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.dev.eventsgeofencing.notification.Reminder
import com.dev.eventsgeofencing.notification.ReminderApplication
import com.dev.eventsgeofencing.utils.sendNotification
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionIntentService : JobIntentService() {

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionIntentService::class.java, JOB_ID,
                intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val geofancingEvent = GeofencingEvent.fromIntent(intent)
        if (geofancingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessage.getErrorString(this, geofancingEvent.errorCode)
            Log.e(LOG_TAG, errorMessage)
            return
        }
        
        handleEvent(geofancingEvent)
    }

    private fun handleEvent(event: GeofencingEvent) {
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val reminder = getFirstReminder(event.triggeringGeofences)
            val message = reminder?.message
            val latLng = reminder?.latLng

            if (message != null && latLng != null) {
                sendNotification(this, message, latLng)
            }
        }
    }

    private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
        val firstGeofence = triggeringGeofences[0]
        return (application as ReminderApplication).getRepository().get(firstGeofence.requestId)
    }
}