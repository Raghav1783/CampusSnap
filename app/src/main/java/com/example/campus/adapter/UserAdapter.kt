package com.example.campus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campus.data.model.Event
import com.example.campus.databinding.EventsRvBinding

class UserAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<UserAdapter.EventViewHolder>() {
    private var listener: OnItemClickListener? = null
    inner class EventViewHolder(private val binding:EventsRvBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(event: Event) {
            binding.txtEventName.text = event.title
            binding.txtClubName.text = event.username
            binding.txtEventPrice.text = event.price

            binding.root.setOnClickListener {
                listener?.onItemClick(event)
            }

        }
    }
    interface OnItemClickListener {
        fun onItemClick(event: Event)
    }



    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
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



