package com.dev.eventsgeofencing.notification

import android.app.Application

class ReminderApplication : Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)
    }

    fun getRepository() = repository
}