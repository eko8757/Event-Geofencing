package com.dev.eventsgeofencing.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dev.eventsgeofencing.BuildConfig
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.model.Response.ResponseEvents
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    var item: ResponseEvents.Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val i = intent
        item = i.getParcelableExtra("idEvent")

        tv_title_detail.text = item?.nama
        tv_tanggal_detail.text = item?.tanggal
        tv_kontak_detail.text = item?.kontak
        tv_email_detail.text = item?.email
        tv_desc_detail.text = item?.keterangan
        Picasso.get().load(BuildConfig.EVENT_PATH + item?.poster).into(img_detail)
    }


}
