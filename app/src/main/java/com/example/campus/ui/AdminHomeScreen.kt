package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.databinding.ActivityAdminHomeScreenBinding

class AdminHomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeScreenBinding
    val eventViewModel:EventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var fab = binding.fabAdd
        fab.setOnClickListener {
            Toast.makeText(this, "yoooo", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
        }
    }
}