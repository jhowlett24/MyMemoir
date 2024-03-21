package com.example.mymemoir1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Initialize layout elements.
    private lateinit var fieldLoginEmail: EditText
    private lateinit var fieldLoginPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonLogin2Register: Button
    private lateinit var buttonGuest: Button

    // Required for FireBase authentication.
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Assign layout elements.
        fieldLoginEmail = findViewById(R.id.fieldLoginEmail)
        fieldLoginPassword = findViewById(R.id.fieldLoginPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonLogin2Register = findViewById(R.id.buttonLogin2Register)
        buttonGuest = findViewById(R.id.buttonGuest)

        // Get the authorization instance to log in.
        auth = FirebaseAuth.getInstance()
        buttonLogin.setOnClickListener() {

            // Gather the information from the required fields.
            val emailLogin: String = fieldLoginEmail.text.toString()
            val passwordLogin: String = fieldLoginPassword.text.toString()

            auth.signInWithEmailAndPassword(emailLogin, passwordLogin).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Account login successful; navigate to the home screen
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        // Account login failed, display a message to the user.
                        Toast.makeText(this, "Incorrect username or password.", Toast.LENGTH_SHORT).show()
                    }
                }        }

        buttonLogin2Register.setOnClickListener() {
            // Navigate to the Home Screen
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        buttonGuest.setOnClickListener() {
            // Navigate to the Home Screen
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}