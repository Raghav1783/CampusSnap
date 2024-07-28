package com.example.campus.ViewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campus.data.model.Event
import com.example.campus.data.repo.EventRepository
import com.example.campus.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {
    private val _events = MutableLiveData<Response<List<Event>>>()
    val event:LiveData<Response<List<Event>>>
        get() = _events

    private val _addevents = MutableLiveData<Response<String>>()
    val addevent:LiveData<Response<String>>
        get() = _addevents

    fun getEvents(){
        _events.value = Response.Loading
        repository.getEvents {
            _events.value = it
        }
    }
    fun addEvent(event:Event){
        _addevents.value = Response.Loading
        repository.addEvent(event){
            _addevents.value = it
        }
    }

    fun Uploadingimg(fileUri: Uri, onResult:(Response<Uri>) -> Unit){
        onResult.invoke(Response.Loading)
        viewModelScope.launch {
            repository.uploadingimg(fileUri,onResult)
        }
    }
    val selectedEvent = MutableLiveData<Event>()

    fun selectEvent(event: Event) {
        selectedEvent.value = event
    }

}