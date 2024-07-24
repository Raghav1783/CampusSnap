package com.example.campus

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.databinding.ActivityCreateEventBinding
import java.util.Calendar

class CreateEvent : AppCompatActivity() {
    private lateinit var binding: ActivityCreateEventBinding
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

}