package com.example.campus.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campus.data.model.Event
import com.example.campus.data.repo.EventRepository
import com.example.campus.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    val repository: EventRepository
): ViewModel() {
    private val _events = MutableLiveData<Response<List<Event>>>()
    val event:LiveData<Response<List<Event>>>
        get() = _events

    private val _addevents = MutableLiveData<Response<List<Event>>>()
    val addevent:LiveData<Response<List<Event>>>
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
}