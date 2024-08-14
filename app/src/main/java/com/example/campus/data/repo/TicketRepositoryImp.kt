package com.example.campus.data.repo

import com.example.campus.data.model.Ticket
import com.example.campus.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TicketRepositoryImp(val database: FirebaseFirestore,val auth: FirebaseAuth):TicketRepository {
    override fun AddTicket(ticket: Ticket, result: (Response<String>) -> Unit) {
        val MyTicketsDcoument = database.collection("Users").document(auth.currentUser?.uid.orEmpty()).collection("tickets").document()
        MyTicketsDcoument.set(ticket).addOnSuccessListener { documentReference ->
            // Document successfully written
            println("DocumentSnapshot added with ID: ")
        }
            .addOnFailureListener { e ->
                // Error occurred
                println("Error adding document: $e")
            }
    }

    override fun getTickets(result: (Response<List<Ticket>>) -> Unit) {
        database.collection("Users").document(auth.currentUser?.uid.orEmpty()).collection("tickets") .get()
            .addOnSuccessListener {
                val tickets = arrayListOf<Ticket>()
                for(document in it){
                    val ticket = document.toObject(Ticket::class.java)
                    tickets.add(ticket)
                }
                result.invoke(Response.Success(tickets))
            }
            .addOnFailureListener {
                result.invoke(Response.Error(it.localizedMessage))
            }
    }


}