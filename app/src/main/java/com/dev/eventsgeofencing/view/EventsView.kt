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
        fun getData()
        fun showData(
            nama: String,
            email: String,
            kontak: String,
            tanggal: String,
            keterangan: String,
            poster: String
        )
    }
}