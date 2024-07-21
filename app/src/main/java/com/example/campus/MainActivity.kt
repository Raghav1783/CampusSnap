package com.example.campus

import android.app.Dialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private var isValidPassword = false
    private var isValidConPassword = false
    private lateinit var loadingDialog: Dialog
    private lateinit var auth: FirebaseAuth;
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        firestore = FirebaseFirestore.getInstance()
        var signupbutton =binding.signUpBtn

        signupbutton.setOnClickListener {
            signUpUser()
        }

    }

    private fun signUpUser() {
        val email = binding.edEmailId.text.toString()
        val password = binding.edPassword.text.toString()
        val name = binding.edUserName.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addToDatabase(email,password,name,"user")
                    Toast.makeText(this, "yoyo", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d(TAG, "signUpUser: ",task.exception)
                    Toast.makeText(this, "yo", Toast.LENGTH_SHORT).show()
                    // Handle the error (e.g., display a toast or log the error)
                }
            }
    }

    private fun addToDatabase(email: String, password: String, name: String, role: String) {
        var currentUserUID = auth.currentUser?.uid ?: ""
        val userDocRef = firestore.collection("users")
        val user = hashMapOf(
            "Uid" to currentUserUID,
            "Name" to name,
            "email" to email,
            "pass" to password,
            "role" to role
        )

        firestore.collection("users").document(currentUserUID)
            .set(user)
            .addOnSuccessListener { documentReference ->
                // Document successfully written
                println("DocumentSnapshot added with ID: ")
            }
            .addOnFailureListener { e ->
                // Error occurred
                println("Error adding document: $e")
            }
    }


}
