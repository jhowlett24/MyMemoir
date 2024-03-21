package com.example.mymemoir1
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WriteNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_notes)

        val editTextNoteTitle: EditText = findViewById(R.id.editTextNoteTitle)
        val editTextNoteContent: EditText = findViewById(R.id.editTextNoteContent)
        val buttonSaveNote: Button = findViewById(R.id.buttonSaveNote)

        buttonSaveNote.setOnClickListener {
            val noteTitle = editTextNoteTitle.text.toString().trim()
            val noteContent = editTextNoteContent.text.toString().trim()

            if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("NotesPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(noteTitle, noteContent)
                editor.apply()

                // Clear the fields or navigate back to the home screen
                editTextNoteTitle.text.clear()
                editTextNoteContent.text.clear()

                // Optionally, navigate back to the Home Activity
                finish()
            } else {
                // Show error message
                Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
