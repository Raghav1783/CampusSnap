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
    fun getEvents(){
        _events.value = repository.getEvents()
    }
}