package com.dev.eventsgeofencing.notification

import androidx.appcompat.app.AppCompatActivity

abstract class BaseReminder : AppCompatActivity() {
    fun getRpository() = (application as ReminderApplication).getRepository()
}