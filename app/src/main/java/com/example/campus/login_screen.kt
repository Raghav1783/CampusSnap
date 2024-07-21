package com.example.campus

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campus.databinding.ActivityLoginScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class login_screen : AppCompatActivity() {
    private lateinit var binding: ActivityLoginScreenBinding
    private lateinit var auth: FirebaseAuth;
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        var loginbutton = binding.idBtnLogin
        var email = binding.idEdtUserName.text.toString()
        var password = binding.idEdtPassword.text.toString()

        loginbutton.setOnClickListener {
            validatelogin(email,password)
        }



    }

    private fun validatelogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User login successful
                    var currentUserUID = auth.currentUser?.uid ?: ""
                    val userDocRef = firestore.collection("users").document(currentUserUID)

                    userDocRef.get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val Role = document.getString("role")
                                if(Role=="user"){
                                    
                                }
                                else{

                                }

                            } else {

                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle failure (e.g., show an error message)
                        }
                } else {
                    // Login failed
                    // Handle the error (e.g., show an error message)
                }
            }

    }
}