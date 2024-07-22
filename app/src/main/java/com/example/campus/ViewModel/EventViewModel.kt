package com.example.campus.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campus.data.model.Event
import com.example.campus.data.repo.EventRepository

class EventViewModel(
    val repository: EventRepository
): ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val event:LiveData<List<Event>>
        get() = _events
    fun getEvents(){
        _events.value = repository.getEvents()
    }
}