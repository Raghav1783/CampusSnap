package com.example.campus.data.repo

import com.example.campus.data.model.Event

interface EventRepository {
    fun  getEvents():List<Event>
}