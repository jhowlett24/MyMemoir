package com.example.mymemoir1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class GetStartedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        val buttonGetStarted: Button = findViewById(R.id.buttonGetStarted)
        buttonGetStarted.setOnClickListener {
            // Navigate to the Home Screen
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}