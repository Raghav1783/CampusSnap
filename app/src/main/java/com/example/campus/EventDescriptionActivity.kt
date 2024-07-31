package com.example.campus

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.data.model.Event
import com.example.campus.databinding.ActivityEventDescriptionBinding
import com.example.campus.util.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class EventDescriptionActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityEventDescriptionBinding
    private lateinit var event: Event
    private lateinit var firestore :FirebaseFirestore
    private lateinit var auth: FirebaseAuth;
    val eventViewModel: EventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("my_prefs", MODE_PRIVATE)
        val eventId = sharedPreferences.getString("event_id", null)

        eventViewModel.getEvent(eventId.toString())
        eventViewModel.event.observe(this@EventDescriptionActivity) { response ->
            when (response) {
                is Response.Error -> {
                    binding.progressbar.visibility = View.GONE
                    // Handle error
                }
                Response.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                Response.None -> {
                    binding.progressbar.visibility = View.GONE
                }
                is Response.Success -> {
                    val event = response.data
                    binding.eventname.text = event.title
                    binding.txtDescription.text = event.description
                    binding.location.text = event.location
                    binding.date.text = event.date
                    binding.txtPrice.text = event.price

                    Glide.with(this)
                        .load(event.image)
                        .into(binding.eventImage)
                }
            }
        }



        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth


        binding.btnRegister.setOnClickListener {
            if (event.price > 0.toString()) {
                startPayment(event.price)
            } else {
                registerUser("Free")
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
            options.put("amount", 100)

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


    private fun registerUser(paymentid:String) {
        var name = auth.currentUser?.displayName.toString()
        var email = auth.currentUser?.email.toString()
        val document = firestore.collection("Event").document("?eventid?").collection("user").document()
        val user = hashMapOf(
            "Uid" to auth.currentUser?.uid.toString(),
            "Name" to name,
            "email" to email,
            "paymentid" to paymentid

        )
        document.set(user);


    }



    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
        if (p0 != null) {
            registerUser(p0)
        }
    }



    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "error $p1", Toast.LENGTH_SHORT).show()
    }


}
