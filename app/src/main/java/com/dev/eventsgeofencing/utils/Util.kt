package com.dev.eventsgeofencing.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.os.Build
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dev.eventsgeofencing.BuildConfig
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.ui.GoogleLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

//notification
//private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"
//fun sendNotification(context: Context, message: String, latLng: LatLng) {
//    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager.getNotificationChannel(
//            NOTIFICATION_CHANNEL_ID) == null) {
//        val name = context.getString(R.string.app_name)
//        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
//        notificationManager.createNotificationChannel(channel)
//    }
//
//    val intent = GoogleLocation.newIntent(context.applicationContext, latLng)
//
//    val stackBuilder = TaskStackBuilder.create(context)
//        .addParentStack(GoogleLocation::class.java)
//        .addNextIntent(intent)
//    val notificationPendingIntent = stackBuilder
//        .getPendingIntent(getUniqueId(), PendingIntent.FLAG_UPDATE_CURRENT)
//    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
//        .setSmallIcon(R.mipmap.ic_launcher)
//        .setContentTitle(message)
//        .setContentIntent(notificationPendingIntent)
//        .setAutoCancel(true)
//        .build()
//
//    notificationManager.notify(getUniqueId(), notification)
//}
//
//fun showReminderInMap(context: Context, map: GoogleMap, reminder: Reminder) {
//    if (reminder.latLng != null) {
//        val latLng = reminder.latLng as LatLng
//        val marker = map.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()))
//        marker.tag = reminder.id
//        if (reminder.radius != null) {
//            val radius = reminder.radius as Double
//            map.addCircle(CircleOptions()
//                .center(reminder.latLng)
//                .radius(radius)
//                .strokeColor(ContextCompat.getColor(context, R.color.colorWhite))
//                .fillColor(ContextCompat.getColor(context, R.color.colorFillMap)))
//        }
//    }
//}
//
//private fun getUniqueId() = ((System.currentTimeMillis() % 10000).toInt())