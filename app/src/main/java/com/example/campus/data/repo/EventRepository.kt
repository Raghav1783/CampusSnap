package com.example.campus.data.repo

import android.net.Uri
import com.example.campus.data.model.Event
import com.example.campus.util.Response

interface EventRepository {
    fun  getEvents(result: (Response<List<Event>>) -> Unit)
    fun addEvent(event: Event, result: (Response<String>) -> Unit)

    suspend fun uploadingimg(fileUri: Uri, onResult:(Response<Uri>) -> Unit)
}