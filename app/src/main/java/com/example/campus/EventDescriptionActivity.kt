package com.example.campus

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.campus.ViewModel.EventViewModel
import com.example.campus.ViewModel.TicketViewModel
import com.example.campus.data.model.Event
import com.example.campus.data.model.Ticket
import com.example.campus.databinding.ActivityEventDescriptionBinding
import com.example.campus.util.Response
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class EventDescriptionActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityEventDescriptionBinding
    private lateinit var event: Event
    private lateinit var firestore :FirebaseFirestore
    private lateinit var auth: FirebaseAuth
//    private var ticketUpdate: TicketUpdate? = null
    val eventViewModel: EventViewModel by viewModels()
    val ticketViewModel:TicketViewModel by viewModels()
    private val storage = FirebaseStorage.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth

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
                    event = response.data
                    binding.eventname.text = event.title
                    binding.txtDescription.text = event.description
                    binding.location.text = event.location
                    binding.date.text = event.date
                    binding.txtPrice.text = event.price

                    Glide.with(this)
                        .load(event.image)
                        .into(binding.eventImage)


                    binding.btnRegister.setOnClickListener {
                        if (event.price.toInt() > 0) {
                            startPayment(event.price)
                        } else {
                            registerUser("Free")
                        }
                    }
                }
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
            options.put("amount", amount.toInt() * 100)

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

    private fun registerUser(paymentId: String) {
        val name = auth.currentUser?.displayName ?: ""
        val email = auth.currentUser?.email ?: ""
        val document = firestore.collection("Event").document(event.id).collection("user").document()
        val user = hashMapOf(
            "Uid" to auth.currentUser?.uid.orEmpty(),
            "Name" to name,
            "email" to email,
            "paymentId" to paymentId
        )
        document.set(user)
    }

    private fun userTicket(paymentId: String) {
        val name = auth.currentUser?.displayName ?: ""
        val email = auth.currentUser?.email ?: ""
        val ticketDocument = firestore.collection("Event").document(event.id)
            .collection("tickets").document(auth.currentUser?.uid.orEmpty())

            val ticketDetails = hashMapOf(
                "Uid" to auth.currentUser?.uid.orEmpty(),
                "eventId" to event.id,
                "status" to "notScanned"
             )
            ticketDocument.set(ticketDetails)


        ticketViewModel.addTicket(
            Ticket(Uid =  auth.currentUser?.uid.orEmpty(),
                EventId = event.id,
                title = event.title,
                event.date))
    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show()
        if (p0 != null) {
            registerUser(p0)
            userTicket(p0)
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this, "Error $p1", Toast.LENGTH_SHORT).show()
    }
}

//    private fun saveQRCodeToFirebaseStorage(bitmap: Bitmap, paymentId: String, callback: (String) -> Unit) {
//        val storageRef = storage.reference
//        val qrCodeRef = storageRef.child("qrcodes/$paymentId.png")
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val data = baos.toByteArray()
//
//        qrCodeRef.putBytes(data)
//            .addOnSuccessListener {
//                qrCodeRef.downloadUrl.addOnSuccessListener { uri ->
//                    callback(uri.toString())
//                }
//            }
//            .addOnFailureListener { exception ->
//                Toast.makeText(this, "Error uploading QR code: ${exception.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

//    private fun generateQRCode(text: String): Bitmap {
//        val qrCodeWriter = QRCodeWriter()
//        val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200)
//        val width = bitMatrix.width
//        val height = bitMatrix.height
//        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//        for (x in 0 until width) {
//            for (y in 0 until height) {
//                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
//            }
//        }
//        return bmp
//    }


