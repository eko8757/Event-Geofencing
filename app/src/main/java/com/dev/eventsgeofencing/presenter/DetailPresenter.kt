package com.dev.eventsgeofencing.presenter

import android.content.Context
import com.dev.eventsgeofencing.model.Response.ResponseEvents
import com.dev.eventsgeofencing.view.EventsView

class DetailPresenter(val view: EventsView.EventsDetail) {

    private lateinit var dataGlobal: ResponseEvents.Event
    private var idEvent: String? = null

    fun extractdata(context: Context, data: ResponseEvents.Event?) {
        val poster = data?.poster.toString()
        val nama = data?.nama.toString()
        val email = data?.email.toString()
        val kontak = data?.kontak.toString()
        val tanggal = data?.tanggal.toString()
        val keterangan = data?.keterangan.toString()
        view.showData(nama, email, kontak, tanggal, keterangan, poster)
        if (data != null) {
            this.dataGlobal = data
        }
        this.idEvent = data?.id
    }
}