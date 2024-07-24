package com.example.campus.data.repo

import com.example.campus.data.model.Event
import com.example.campus.util.Response
import com.google.firebase.firestore.FirebaseFirestore

class EventRepositoryImp(val database:FirebaseFirestore):EventRepository {
    override fun getEvents(result: (Response<List<Event>>) -> Unit) {
        database.collection("Event").get()
            .addOnSuccessListener {
                val events = arrayListOf<Event>()
                for(document in it){
                    val event = document.toObject(Event::class.java)
                    events.add(event)
                }
                result.invoke(Response.Success(events))
            }
            .addOnFailureListener {
                result.invoke(Response.Error(it.localizedMessage))
            }

    }

    override fun addEvent(event: Event, result: (Response<List<Event>>) -> Unit) {
        database.collection("Event").document().set(event)
            .addOnSuccessListener { documentReference ->
                // Document successfully written
                println("DocumentSnapshot added with ID: ")
            }
            .addOnFailureListener { e ->
                // Error occurred
                println("Error adding document: $e")
            }
    }


}