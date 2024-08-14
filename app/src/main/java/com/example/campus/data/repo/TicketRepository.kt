package com.example.campus.data.repo

import com.example.campus.data.model.Ticket
import com.example.campus.util.Response

interface TicketRepository {
        fun AddTicket(ticket: Ticket, result: (Response<String>) -> Unit)

        fun  getTickets(result: (Response<List<Ticket>>) -> Unit)
}