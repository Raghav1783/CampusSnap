package com.example.campus.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campus.R
import com.example.campus.adapter.MyEventsAdapter
import com.example.campus.data.model.Event
import com.example.campus.databinding.ActivityMyEventsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.io.ByteArrayOutputStream

class MyEventsActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var binding: ActivityMyEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyEventsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(R.layout.activity_my_events)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        fetchEvents()
    }

    private fun setupRecyclerView() {
        binding.rvMyEvent.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchEvents() {
        firestore.collection("events")
            .get()
            .addOnSuccessListener { result ->
                val events = result.toObjects(Event::class.java)
                events.forEach { event ->
                    val qrCodeBitmap = generateQRCode(event.id)
                    val qrCodeUrl = saveQRCodeToFirebaseStorage(qrCodeBitmap, event.id)
                    event.qrCodeUrl = qrCodeUrl
                }
                binding.rvMyEvent.adapter = MyEventsAdapter(events)
            }
            .addOnFailureListener { exception ->
                // Handle error
                android.util.Log.e("MyEventsActivity", "Error fetching events", exception)
            }
    }

    private fun generateQRCode(text: String): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    private fun saveQRCodeToFirebaseStorage(bitmap: Bitmap, eventId: String): String {
        val storageRef = storage.reference
        val qrCodeRef = storageRef.child("qrcodes/$eventId.png")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        var qrCodeUrl = ""
        qrCodeRef.putBytes(data)
            .addOnSuccessListener {
                qrCodeRef.downloadUrl.addOnSuccessListener { uri ->
                    qrCodeUrl = uri.toString()
                }
            }
            .addOnFailureListener { exception ->
                // Handle error

            }

        return qrCodeUrl
    }
}