package com.example.campus.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campus.data.model.Ticket
import com.example.campus.data.repo.TicketRepository
import com.example.campus.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val repository:
    TicketRepository
): ViewModel() {
    private val _addticket = MutableLiveData<Response<String>>()

    private val _tickets = MutableLiveData<Response<List<Ticket>>>()

    val tickets: LiveData<Response<List<Ticket>>>
        get() = _tickets
    fun addTicket(ticket: Ticket){
        _addticket.value = Response.Loading
        repository.AddTicket(ticket){
            _addticket.value = it
        }
    }

    fun getTickets(){
        _tickets.value = Response.Loading
        repository.getTickets {
            _tickets.value = it
        }
    }
}