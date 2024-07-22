package com.example.campus.data.repo

import com.example.campus.data.model.Event
import com.example.campus.util.Response
import com.google.firebase.firestore.FirebaseFirestore

class EventRepositoryImp(val database:FirebaseFirestore):EventRepository {
    override fun getEvents(): Response<List<Event>> {
        val events: List<Event> = arrayListOf()

        val response: Response<List<Event>> = Response.Success(events)

        return response
    }


}