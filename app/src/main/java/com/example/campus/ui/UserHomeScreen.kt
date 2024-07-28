package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus.EventDescriptionActivity
import com.example.campus.R
import com.example.campus.ViewModel.EventViewModel

import com.example.campus.adapter.UserAdapter
import com.example.campus.databinding.ActivityUserHomeScreenBinding
import com.example.campus.util.Response

class UserHomeScreen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityUserHomeScreenBinding
    val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        eventViewModel.getEvents()
        eventViewModel.event.observe(this@UserHomeScreen) { response ->
            when (response) {
                is Response.Error -> {
                    binding.progressbar.visibility = View.GONE
                    // Handle error
                }
                Response.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                Response.None -> {
                    binding.progressbar.visibility = View.GONE
                }
                is Response.Success -> {
                    binding.progressbar.visibility = View.GONE
                    val adapter = UserAdapter(response.data) { event ->
                        val intent = Intent(this, EventDescriptionActivity::class.java).apply {
                            putExtra("event_id", event.id)
                        }
                        startActivity(intent)
                    }
                    binding.recyclerView.adapter = adapter
                }
            }
        }
    }
}