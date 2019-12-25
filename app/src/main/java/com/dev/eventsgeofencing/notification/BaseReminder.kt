package com.dev.eventsgeofencing.notification

import androidx.appcompat.app.AppCompatActivity

abstract class BaseReminder : AppCompatActivity() {
    fun getRepository() = (application as ReminderApplication).getRepository()
}