package com.example.campus.di

import com.example.campus.data.repo.TicketUpdate
import com.example.campus.ui.MyEventsActivity

object TicketUpdateManager{
    private var listener: TicketUpdate? = null

    fun registerListener(listener: MyEventsActivity) {
        this.listener = listener
    }

    fun unregisterListener() {
        this.listener = null
    }

    fun notifyTicketUpdated(eventName: String, qrCodeUrl: String) {
        listener?.onTicketUpdated(eventName, qrCodeUrl)
    }
}
