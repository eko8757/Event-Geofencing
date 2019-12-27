package com.dev.eventsgeofencing.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dev.eventsgeofencing.BuildConfig
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.model.response.ResponseEvents
import com.dev.eventsgeofencing.view.EventsView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), EventsView.EventsDetail, View.OnClickListener {

    var item: ResponseEvents.Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        loadData()
        btn_go_to_location.setOnClickListener(this)
    }

    private fun loadData() {
        val i = intent
        item = i.getParcelableExtra("idEvent")

        tv_title_detail.text = item?.nama
        tv_tanggal_detail.text = item?.tanggal
        tv_kontak_detail.text = item?.kontak
        tv_email_detail.text = item?.email
        tv_desc_detail.text = item?.keterangan
        Picasso.get().load(BuildConfig.EVENT_PATH + item?.poster).into(img_detail)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_go_to_location -> goToLocation()
        }
    }

    override fun goToLocation() {
        val i = Intent(this, GoogleLocation::class.java)
        i.putExtra("longitude", item?.longitude)
        i.putExtra("latitude", item?.latitude)
        startActivity(i)
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }
}
