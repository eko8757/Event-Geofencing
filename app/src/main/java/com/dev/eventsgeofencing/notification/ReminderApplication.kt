package com.dev.eventsgeofencing.notification

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs

@SuppressLint("Registered")
class ReminderApplication : Application() {

    private lateinit var repository: ReminderRepository

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)
        pref()
    }
    fun getRepository() = repository

    private fun pref() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}