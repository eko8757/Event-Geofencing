package com.dev.eventsgeofencing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.view.EventsView

class ProfileActivity : AppCompatActivity(), EventsView.ProfileView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun btnUpdate() {

    }
}
