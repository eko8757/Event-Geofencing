package com.dev.eventsgeofencing.view

import android.content.Context
import com.dev.eventsgeofencing.model.Response.ResponseEvents

interface EventsView {

    interface EventsList {
        fun showProgress()
        fun hideProgress()
        fun showToast(msg: String)
        fun showData(data: ArrayList<ResponseEvents.Event>)
    }

    interface EventsDetail {
       fun goToLocation()
    }
}