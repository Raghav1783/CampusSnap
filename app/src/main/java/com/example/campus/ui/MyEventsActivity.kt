package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campus.ViewModel.TicketViewModel
import com.example.campus.adapter.MyEventsAdapter
import com.example.campus.data.model.Ticket
import com.example.campus.databinding.ActivityMyEventsBinding
import com.example.campus.util.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEventsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyEventsBinding
    private val ticketViewModel: TicketViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeUserTickets()
        fetchUserTickets()

    }

    private fun setupRecyclerView() {
        binding.rvMyEvent.layoutManager = LinearLayoutManager(this)
    }

    private fun observeUserTickets() {
        ticketViewModel.getTickets()
        ticketViewModel.tickets.observe(this) { response ->
            when (response) {
                is Response.Loading -> {
                    binding.progressBar2.visibility = View.VISIBLE
                }
                is Response.Success -> {
                    binding.progressBar2.visibility = View.GONE

                    val adapter = MyEventsAdapter(response.data).apply {
                        setOnItemClickListener(object : MyEventsAdapter.OnItemClickListener {
                            override fun onItemClick(ticket: Ticket) {
                                val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
                                val code = ticket.EventId +'$'+ticket.Uid
                                sharedPreferences.edit().putString("ticket_id", code).apply()

                                val intent = Intent(this@MyEventsActivity, TicketQr::class.java).apply {

                                }
                                startActivity(intent)
                            }
                        })
                    }
                    binding.rvMyEvent.adapter = adapter
                }
                is Response.Error -> {
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(this, "Error fetching tickets: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                Response.None -> {
                    binding.progressBar2.visibility = View.GONE
                }
            }
        }
    }

    private fun fetchUserTickets() {
        ticketViewModel.getTickets()
    }
}
