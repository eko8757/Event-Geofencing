package com.dev.eventsgeofencing.view

import com.dev.eventsgeofencing.model.response.ResponseEvents

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