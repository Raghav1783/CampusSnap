package com.example.campus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campus.R
import com.example.campus.data.model.Event

class MyEventsAdapter (private val events: List<Event>): RecyclerView.Adapter<MyEventsAdapter.EventViewHolder>(){

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.rv_myEvent)
        val qrCodeImage: ImageView = itemView.findViewById(R.id.img_qr)
    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.myevents_rv, parent, false)
            return EventViewHolder(view)
        }
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.title
        if (event.qrCodeUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(event.qrCodeUrl)
                .into(holder.qrCodeImage)
        }
    }

    override fun getItemCount() = events.size
}