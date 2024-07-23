package com.example.campus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.campus.R
import com.example.campus.data.model.UserData
import com.example.campus.databinding.EventsRvBinding

class UserAdapter(private val events: List<UserData>) :
    RecyclerView.Adapter<UserAdapter.EventViewHolder>() {
    inner class EventViewHolder(private val binding:EventsRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: UserData) {
            binding.txtEventName.text = event.eventName
            binding.txtClubName.text = event.clubName
            binding.txtEventPrice.text = event.eventPrice
            binding.txtDatePosted.text = event.datePosted
            // Assuming imageUrl is a URL; otherwise, replace with setImageResource for local resources
            Glide.with(binding.imgClub.context)
                .load(event.imageUrl)
                .into(binding.imgClub)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = EventsRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size
}



