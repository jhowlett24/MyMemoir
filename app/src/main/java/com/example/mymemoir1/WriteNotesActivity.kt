package com.example.mymemoir1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class WriteNotesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var spinnerTimeDuration: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_notes)

        // Initialize FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize views
        val editTextNoteTitle: EditText = findViewById(R.id.editTextNoteTitle)
        val editTextNoteContent: EditText = findViewById(R.id.editTextNoteContent)
        val buttonSaveNote: Button = findViewById(R.id.buttonSaveNote)
        spinnerTimeDuration = findViewById(R.id.spinnerTimeDuration)

        // Setup spinner
        val timeDurationOptions = arrayOf("5 Seconds", "1 Hour", "6 Hours",
            "1 Day", "5 Days", "1 Week", "2 Weeks", "1 Month", "6 Months", "1 Year", )
        val spinnerAdapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, timeDurationOptions)
        spinnerTimeDuration.adapter = spinnerAdapter

        // Save note button click listener
        buttonSaveNote.setOnClickListener {
            val noteTitle = editTextNoteTitle.text.toString().trim()
            val noteContent = editTextNoteContent.text.toString().trim()
            val selectedTimeDuration = spinnerTimeDuration.selectedItem.toString()

            if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                saveNoteToFirestore(noteTitle, noteContent, selectedTimeDuration)

                // Clear the fields
                editTextNoteTitle.text.clear()
                editTextNoteContent.text.clear()
            } else {
                // Show error message
                Toast.makeText(this, "Please fill in both fields.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNoteToFirestore(noteTitle: String, noteContent: String, timeDuration: String) {
        val notesCollection = firestore.collection("notes")
        val noteData = hashMapOf(
            "title" to noteTitle,
            "content" to noteContent,
            "timeDuration" to timeDuration
        )

        notesCollection.add(noteData)
            .addOnSuccessListener {
                Toast.makeText(this, "Note saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving note: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
