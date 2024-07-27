package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.databinding.ActivityAdminHomeScreenBinding
//import com.journeyapps.barcodescanner.ScanContract
//import com.journeyapps.barcodescanner.ScanOptions

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
//        binding.scanQrButton.setOnClickListener {
//            val options = ScanOptions()
//            qrLauncher.launch(options)
//        }
    }

//    private val qrLauncher = registerForActivityResult(ScanContract()) { result ->
//        if (result.contents != null) {
//            // Handle scanned result
//            Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_LONG).show()
//        } else {
//            Toast.makeText(this, "QR Scan failed", Toast.LENGTH_SHORT).show()
//        }
//    }
}