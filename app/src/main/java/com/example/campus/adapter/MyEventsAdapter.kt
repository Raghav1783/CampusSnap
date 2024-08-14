package com.example.campus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.campus.data.model.Ticket
import com.example.campus.databinding.MyeventsRvBinding

class MyEventsAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<MyEventsAdapter.TicketViewHolder>() {

    private var listener: OnItemClickListener? = null

    inner class TicketViewHolder(private val binding: MyeventsRvBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket) {
            binding.txtMyEvent.text = ticket.title
//            if (ticket.qrCodeUrl.isNotEmpty()) {
//                Glide.with(binding.root.context)
//                    .load(ticket.qrCodeUrl)
//                    .into(binding.imgQr)
//            }

            binding.root.setOnClickListener {
                listener?.onItemClick(ticket)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(ticket: Ticket)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = MyeventsRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount() = tickets.size
}
