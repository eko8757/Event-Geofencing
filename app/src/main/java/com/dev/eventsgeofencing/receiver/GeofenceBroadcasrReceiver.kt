package com.dev.eventsgeofencing.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GeofenceBroadcasrReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        GeofenceTransitionIntentService.enqueueWork(context, intent)
    }
}