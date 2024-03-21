package com.example.mymemoir1

// Code via Codes Easy
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    // Initialize layout elements.
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword1: EditText
    private lateinit var fieldPassword2: EditText
    private lateinit var buttonRegister: Button
    private lateinit var buttonRegister2Login: Button

    // Required for FireBase authentication.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Assign layout elements.
        fieldEmail = findViewById(R.id.fieldEmail)
        fieldPassword1 = findViewById(R.id.fieldPassword1)
        fieldPassword2 = findViewById(R.id.fieldPassword2)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonRegister2Login = findViewById(R.id.buttonRegister2Login)

        buttonRegister.setOnClickListener {

            // Gather the information from the required fields.
            val email: String = fieldEmail.text.toString()
            val password1: String = fieldPassword1.text.toString()
            val password2: String = fieldPassword2.text.toString()

            // Check if all the fields have been filled in.
            if (TextUtils.isEmpty(email) or TextUtils.isEmpty(password1) or TextUtils.isEmpty(password2)) {
                Toast.makeText(this, "All fields must be filled.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the two password fields match.
            if (password1 != password2) {
                Toast.makeText(this, "The passwords must match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get the authorization instance and attempt to create a new account given the email and password.
            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account creation successful.
                    // val user = auth.currentUser
                    Toast.makeText(this, "Authentication successful.", Toast.LENGTH_SHORT).show()
                }
                else {
                    // Account creation failed, display a message to the user.
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonRegister2Login.setOnClickListener() {
            // Navigate to the Home Screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}