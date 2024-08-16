package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.databinding.ActivityAdminHomeScreenBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class AdminHomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityAdminHomeScreenBinding
    val eventViewModel:EventViewModel by viewModels()
    private val firestore = FirebaseFirestore.getInstance()
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
        binding.scanQrButton.setOnClickListener {
            val options = ScanOptions()
            qrLauncher.launch(options)
      }
    }

   private val qrLauncher = registerForActivityResult(ScanContract()) { result ->
       if (result.contents != null) {
           CheckinUser(result.contents)
           Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_LONG).show()
       } else {


           Toast.makeText(this, "QR Scan failed", Toast.LENGTH_SHORT).show()
       }
   }

    private fun CheckinUser(result: String) {
        val parts = result.split("$")
        val eventId = parts[0]
        val uid = parts[1]

        val documentRef = firestore.collection("Event").document(eventId)
            .collection("tickets").document(uid)

        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentStatus = document.getString("status")
                    if (currentStatus == "Scanned") {

                        Toast.makeText(this, "User already checked in", Toast.LENGTH_SHORT).show()
                    } else {

                        documentRef.update("status", "Scanned")
                            .addOnSuccessListener {

                                Toast.makeText(this, "User scanned in successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->

                                Toast.makeText(this, "Failed to check in user: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    // Document does not exist
                    Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Handle failure in retrieving the document
                Toast.makeText(this, "Error checking status: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun validateQrCode(scannedData: String) {
    // Replace with the actual logic to get the expected QR code data
    val userId = getUserId()
    firestore.collection("events")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
            var matchFound = false
            for (document in result.documents) {
                val eventId = document.getString("id")
                val expectedQrCodeData = "$eventId$userId"
                if (scannedData == expectedQrCodeData) {
                    matchFound = true
                    break
                }
            }

            if (matchFound) {
                Toast.makeText(this, "Success: QR code matches", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error: QR code does not match", Toast.LENGTH_SHORT).show()
            }
        }
        .addOnFailureListener { exception ->
            // Handle error
            Toast.makeText(this, "Error fetching events", Toast.LENGTH_SHORT).show()
        }
}

private fun getUserId(): String {
    // Replace this with the actual logic to retrieve the current user's ID
    return "exampleUserId"
}
}