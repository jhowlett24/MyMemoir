package com.example.mymemoir1
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WriteNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_notes)

        val editTextNoteTitle: EditText = findViewById(R.id.editTextNoteTitle)
        val editTextNoteContent: EditText = findViewById(R.id.editTextNoteContent)
        val buttonSaveNote: Button = findViewById(R.id.buttonSaveNote)
        val spinnerTimeDuration: Spinner = findViewById(R.id.spinnerTimeDuration)

        // Set up the Spinner with the adapter
        ArrayAdapter.createFromResource(
            this,
            R.array.time_durations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTimeDuration.adapter = adapter
        }

        buttonSaveNote.setOnClickListener {
            val noteTitle = editTextNoteTitle.text.toString().trim()
            val noteContent = editTextNoteContent.text.toString().trim()
            val selectedTimeDuration = spinnerTimeDuration.selectedItem.toString()

            if (noteTitle.isNotEmpty() && noteContent.isNotEmpty()) {
                val sharedPreferences = getSharedPreferences("NotesPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString(noteTitle, noteContent)
                editor.putString("$noteTitle-duration", selectedTimeDuration)
                editor.apply()

                editTextNoteTitle.text.clear()
                editTextNoteContent.text.clear()

                finish() // Optionally navigate back
            } else {
                Toast.makeText(this, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
