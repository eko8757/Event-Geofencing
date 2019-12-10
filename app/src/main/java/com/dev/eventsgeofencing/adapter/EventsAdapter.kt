package com.dev.eventsgeofencing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.dev.eventsgeofencing.BuildConfig
import com.dev.eventsgeofencing.R
import com.dev.eventsgeofencing.model.Response.ResponseEvents
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list.view.*

class EventsAdapter(private val items: List<ResponseEvents.Event>, private val listener: (ResponseEvents.Event) -> Unit )
    : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindData(items[position], listener)
    }

    class EventViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bindData(item: ResponseEvents.Event, listener: (ResponseEvents.Event) -> Unit) {
            itemView.tv_title_item_list.text = item.nama
            itemView.tv_tgl_item_list.text = item.tanggal
            itemView.tv_desc_item_list.text = item.keterangan
            Picasso.get().load(BuildConfig.EVENT_PATH + item.poster).into(itemView.img_item_list)

            itemView.setOnClickListener {
                listener(item)
            }
        }
    }
}