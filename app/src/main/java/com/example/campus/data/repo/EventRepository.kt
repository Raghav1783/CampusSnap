package com.example.campus.data.repo

import com.example.campus.data.model.Event
import com.example.campus.util.Response

interface EventRepository {
    fun  getEvents():Response<List<Event>>
}