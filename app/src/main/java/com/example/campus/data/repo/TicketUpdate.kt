package com.example.campus.data.repo

interface TicketUpdate  {
    fun onTicketUpdated(eventName: String, qrCodeUrl: String)
}