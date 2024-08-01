package com.example.campus.ui

import android.os.Bundle
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.adapter.MyEventsAdapter
import com.example.campus.databinding.ActivityMyEventsBinding
import com.example.campus.di.TicketUpdateManager
import com.example.campus.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyEventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyEventsBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TicketUpdateManager.registerListener(this)

        setupRecyclerView()
        observeUserTickets()
        fetchUserTickets()
    }

    override fun onTicketUpdated(eventName: String, qrCodeUrl: String) {

        fetchUserTickets()
    }

    private fun setupRecyclerView() {
        binding.rvMyEvent.layoutManager = LinearLayoutManager(this)
    }

    private fun observeUserTickets() {
        eventViewModel.events.observe(this, Observer { response ->
            when (response) {
                is Response.Loading -> {
                    // Show loading indicator if needed
                }
                is Response.Success -> {
                    val tickets = response.data
                    binding.rvMyEvent.adapter = MyEventsAdapter(tickets)
                }
                is Response.Error -> {
                    Toast.makeText(this, "Error fetching tickets: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                Response.None -> {
                    // Handle no action case
                }
            }
        })
    }

    private fun fetchUserTickets() {
        eventViewModel.getEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister listener
        TicketUpdateManager.unregisterListener()
    }
}