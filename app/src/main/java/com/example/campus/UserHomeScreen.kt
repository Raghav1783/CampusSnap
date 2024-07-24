package com.example.campus

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventViewModel.getEvents()
        eventViewModel.event.observe(this@UserHomeScreen){
            when(it){
                is Response.Error -> {

                }

                Response.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }

                Response.None -> {

                }

                is Response.Success -> {
                    binding.progressbar.visibility = View.GONE
                    binding.recyclerView.apply{
                        adapter = UserAdapter(it.data)
                        layoutManager = LinearLayoutManager(context)
                    }
                }
            }
        }
    }

}