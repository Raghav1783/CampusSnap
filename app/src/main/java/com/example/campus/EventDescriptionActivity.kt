package com.example.campus

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.data.model.Event
import com.example.campus.databinding.ActivityEventDescriptionBinding
import com.example.campus.ui.UserHomeScreen
import com.razorpay.Checkout
import org.json.JSONObject

class EventDescriptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventDescriptionBinding
    private lateinit var event: Event
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDescriptionBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnRegister.setOnClickListener {
            if (event.price > 0.toString()) {
                startPayment(event.price)
            } else {
                registerUser()
            }
        }
    }

    private fun startPayment(amount: String) {
        val activity = this
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_F8kYQL3OJcz3JG")

        try {
            val options = JSONObject()
            options.put("name", event.title)
            options.put("description", event.description)
            options.put("image", "https://example.com/your_logo")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", amount * 100)

            val prefill = JSONObject()
            prefill.put("email", "user@example.com")
            prefill.put("contact", "9876543210")

            options.put("prefill", prefill)
            checkout.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    fun onPaymentSuccess(razorpayPaymentID: String?) {
        Toast.makeText(this, "Payment Successful: $razorpayPaymentID", Toast.LENGTH_SHORT).show()
        registerUser()
    }

    fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment failed: $response", Toast.LENGTH_SHORT).show()
    }

    private fun registerUser() {
        // Implement the registration logic
        // For example, open a new activity or show a success message
        startActivity(Intent(this, UserHomeScreen::class.java))

    }
}

private operator fun String.times(i: Int) {

}
