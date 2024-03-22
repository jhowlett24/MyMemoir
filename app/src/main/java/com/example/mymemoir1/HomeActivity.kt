package com.example.mymemoir1
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val buttonViewGoals: Button = findViewById(R.id.buttonViewGoals)
        val buttonSetGoals: Button = findViewById(R.id.buttonSetGoals)
        val buttonViewNotes: Button = findViewById(R.id.buttonViewNotes)
        val buttonWriteNotes: Button = findViewById(R.id.buttonWriteNotes)

        buttonViewNotes.setOnClickListener {
            val intent = Intent(this, ViewNotesActivity::class.java)
            startActivity(intent)
        }

        buttonWriteNotes.setOnClickListener {
            val intent = Intent(this, WriteNotesActivity::class.java)
            startActivity(intent)
        }

        buttonViewGoals.setOnClickListener {
            val intent = Intent(this, ViewGoalsActivity::class.java)
            startActivity(intent)
        }

        buttonSetGoals.setOnClickListener {
            val intent = Intent(this, SetGoalsActivity::class.java)
            startActivity(intent)
        }
    }
}