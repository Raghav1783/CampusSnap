package com.example.campus.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.campus.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginScreen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val loginButton = binding.idBtnLogin
        loginButton.setOnClickListener {
            val email = binding.idEdtUserName.text.toString()
            val password = binding.idEdtPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                validateLogin(email, password)
            } else {

                Toast.makeText(this, "Please fill in both email and password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUserUID = auth.currentUser?.uid
                    if (currentUserUID != null) {
                        val userDocRef = firestore.collection("users").document(currentUserUID)

                        userDocRef.get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role")
                                    if (role == "user") {
                                        val intent = Intent(this, UserHomeScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        val intent = Intent(this, AdminHomeScreen::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    // Document does not exist
                                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                // Handle failure (e.g., show an error message)
                                Toast.makeText(this, "Failed to retrieve user data.", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Current user UID is null
                        Toast.makeText(this, "Failed to get user ID.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Login failed
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
