package com.example.campus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.databinding.ActivityAdminHomeScreenBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AdminHomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeScreenBinding
    val eventViewModel:EventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var fab = binding.fabAdd
        fab.setOnClickListener {
            val intent = Intent(this, CreateEvent::class.java)
            startActivity(intent)
            finish()
        }
        binding.scanQrButton.setOnClickListener {
            val options = ScanOptions()
            qrLauncher.launch(options)
        }
    }

    private val qrLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            // Handle scanned result
            Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "QR Scan failed", Toast.LENGTH_SHORT).show()
        }
    }
}