package com.example.campus.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.data.model.Event
import com.example.campus.databinding.ActivityCreateEventBinding
import com.example.campus.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
@AndroidEntryPoint
class CreateEvent : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
    val viewModel: EventViewModel by viewModels()
    private var imageUrl: String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.tvDate.setOnClickListener { setDate() }
        binding.ivDate.setOnClickListener { setDate() }
        binding.tvDateTxt.setOnClickListener { setDate() }
        binding.dateLayout.setOnClickListener { setDate() }

        binding.uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }

        binding.CreateButton.setOnClickListener {
            if (validateInput()) {
                val currentUserUID = auth.currentUser?.uid ?: return@setOnClickListener
                firestore.collection("users").document(currentUserUID).get().addOnSuccessListener { document ->
                    val username = document.getString("name") ?: "Unknown User"
                    viewModel.addEvent(
                        Event(
                            id = "",
                            username = username,
                            title = binding.titleTextView.text.toString(),
                            price = binding.priceEditText.text.toString(),
                            image = imageUrl,
                            description = binding.descriptionTextView.text.toString(),
                            date = binding.tvDate.text.toString(),
                            location = binding.locationTextView.text.toString()
                        )
                    )
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to fetch user data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (binding.titleTextView.text.toString().isEmpty()) {
            binding.titleTextView.error = "Title is required"
            isValid = false
        }

        if (binding.descriptionTextView.text.toString().isEmpty()) {
            binding.descriptionTextView.error = "Description is required"
            isValid = false
        }

        if (binding.tvDate.text.toString().isEmpty() || binding.tvDate.text.toString() == "dd_mm_yy") {
            binding.tvDate.error = "Date is required"
            isValid = false
        }

        if (binding.locationTextView.text.toString().isEmpty()) {
            binding.locationTextView.error = "Location is required"
            isValid = false
        }

        val priceText = binding.priceEditText.text.toString()
        if (priceText.isEmpty()) {
            binding.priceEditText.error = "Price is required"
            isValid = false
        } else {
            try {
                val price = priceText.toDouble()
                if (price <= 0) {
                    binding.priceEditText.error = "Price must be greater than zero"
                    isValid = false
                }
            } catch (e: NumberFormatException) {
                binding.priceEditText.error = "Invalid price"
                isValid = false
            }
        }

        if (imageUrl.isEmpty()) {
            Toast.makeText(this, "Image is required", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this@CreateEvent,
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedMonth = if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else "${selectedMonth + 1}"
                val formattedDay = if (selectedDay < 10) "0$selectedDay" else "$selectedDay"
                binding.tvDate.text = "$formattedDay-$formattedMonth-$selectedYear"
            },
            year, month, day
        )

        datePicker.datePicker.minDate = calendar.timeInMillis

        datePicker.setOnShowListener {
            val positiveButton = datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(ContextCompat.getColor(this@CreateEvent, android.R.color.black))
        }

        datePicker.show()
    }

    private val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            viewModel.Uploadingimg(it.data!!.data!!) { response ->
                when (response) {
                    is Response.Success -> imageUrl = response.data.toString()
                    is Response.Error -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    is Response.Loading -> Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show()
                    is Response.None -> Toast.makeText(this, "No response", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
