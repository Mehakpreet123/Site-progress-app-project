package com.example.siteprogress

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val roleSpinner = findViewById<Spinner>(R.id.roleSpinner)
        val signUpButton = findViewById<Button>(R.id.signUpButton)

        // Dropdown for roles
        val roles = arrayOf("Admin", "Engineer", "Worker")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter

        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()

            if (validateInputs(name, email, password)) {
                checkIfEmailExists(email) { exists ->
                    if (exists) {
                        Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
                    } else {
                        registerUser(name, email, password, role)
                    }
                }
            }
        }
    }

    private fun validateInputs(name: String, email: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
        db.collection("users").whereEqualTo("email", email).get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty) // Returns true if email exists
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun registerUser(name: String, email: String, password: String, role: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val userId = auth.currentUser?.uid
                val user = hashMapOf(
                    "userId" to userId,
                    "name" to name,
                    "email" to email,
                    "role" to role
                )

                // Save user details to Firestore
                db.collection("users").document(userId!!)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, login::class.java))
                        finish()
                    }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Registration failed: ${exception.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}
