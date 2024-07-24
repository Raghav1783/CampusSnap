package com.example.campus.data.repo

import com.example.campus.data.model.Event
import com.example.campus.util.Response

interface EventRepository {
    fun  getEvents(result: (Response<List<Event>>) -> Unit)
    fun addEvent(event: Event, result: (Response<List<Event>>) -> Unit)
}