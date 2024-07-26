package com.example.campus

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.data.model.Event
import com.example.campus.databinding.ActivityCreateEventBinding
import com.example.campus.util.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CreateEvent : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
    val viewmodel :EventViewModel by viewModels()
    private var imageUrl: String = ""
    private lateinit var auth: FirebaseAuth;
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_event)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvDate.setOnClickListener {
            setDate()
        }
        binding.ivDate.setOnClickListener {
            setDate()
        }
        binding.tvDateTxt.setOnClickListener {
            setDate()
        }
        binding.dateLayout.setOnClickListener {
            setDate()
        }

        binding.uploadButton.setOnClickListener {
            val intent =Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)
        }
        binding.CreateButton.setOnClickListener {
            if(validation()){
                var currentUserUID = auth.currentUser?.uid ?: ""
                val username = firestore.collection("users").document(currentUserUID).collection("Name").toString()


                viewmodel.addEvent(
                    Event(id = "",
                    username = username,
                    title = binding.titleTextView.text.toString(),
                    price = binding.priceEditText.text.toString(),
                    image = imageUrl,
                    description = binding.descriptionTextView.text.toString(),
                    date = binding.tvDate.text.toString(),
                    location = binding.tvDate.text.toString()
                    )
                )
            }
        }
    }



    private fun validation(): Boolean {
        var isValid = true

        // Validate Title
        if (binding.titleTextView.text.toString().isNullOrEmpty()) {
            binding.titleTextView.error = "Title is required"
            isValid = false
        }

        // Validate Description
        if (binding.descriptionTextView.text.toString().isNullOrEmpty()) {
            binding.descriptionTextView.error = "Description is required"
            isValid = false
        }

        // Validate Date
        if (binding.tvDate.text.toString().isNullOrEmpty() || binding.tvDate.text.toString() == "dd_mm_yy") {
            binding.tvDate.error = "Date is required"
            isValid = false
        }

        // Validate Location
        if (binding.locationTextView.text.toString().isNullOrEmpty()) {
            binding.locationTextView.error = "Location is required"
            isValid = false
        }

        // Validate Price
        val priceText = binding.priceEditText.text.toString()
        if (priceText.isNullOrEmpty()) {
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
        if(imageUrl.isNullOrEmpty()){
            isValid = false
        }

        return isValid
    }


    private fun setDate() {
        val calendar: Calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this@CreateEvent,
            { datePicker, selectedYear, selected_month, selectedDay ->
                val selectedMonth = selected_month + 1
                binding.tvDate.text =
                    "${if (selectedDay < 10) "0$selectedDay" else selectedDay}-${if (selectedMonth < 10) "0$selectedMonth" else selectedMonth}-$selectedYear"
            }, year, month, day
        )

        datePicker.datePicker.minDate = calendar.timeInMillis

        // Change the color of the OK button
        datePicker.setOnShowListener {
            val positiveButton = datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    this@CreateEvent,
                    android.R.color.black
                )
            )
        }

        datePicker.show()
    }
    val imageLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK ){
            if(it.data!=null){
                 viewmodel.Uploadingimg(it.data!!.data!!){
                     when(it){
                         is Response.Success ->{
                             imageUrl = it.data.toString()
                         }
                         is Response.Error -> {
                             Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                         }
                         is Response.Loading ->{
                             Toast.makeText(this, "loading", Toast.LENGTH_SHORT).show()
                         }
                         is Response.None -> {
                             Toast.makeText(this, "none", Toast.LENGTH_SHORT).show()
                         }
                     }
                }
            }
        }
    }

}