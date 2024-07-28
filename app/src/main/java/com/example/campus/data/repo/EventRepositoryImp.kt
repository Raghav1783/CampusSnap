package com.example.campus.data.repo

import android.net.Uri
import com.example.campus.data.model.Event
import com.example.campus.util.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EventRepositoryImp(val database:FirebaseFirestore,val StorageReference:StorageReference):EventRepository {
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

    override fun addEvent(event: Event, result: (Response<String>) -> Unit) {
        var document = database.collection("Event").document()
        event.id = document.id

            document
            .set(event)
            .addOnSuccessListener { documentReference ->
                // Document successfully written
                println("DocumentSnapshot added with ID: ")
            }
            .addOnFailureListener { e ->
                // Error occurred
                println("Error adding document: $e")
            }
    }

    override suspend fun uploadingimg(fileUri: Uri, onResult: (Response<Uri>) -> Unit) {
        try {
            val uri: Uri = withContext(Dispatchers.IO){
                StorageReference.putFile(fileUri).await().storage.downloadUrl.await()
            }
            onResult.invoke(Response.Success(uri))
        }
        catch(e:FirebaseFirestoreException){
            onResult.invoke(Response.Error(e.message))
        }

    }


}