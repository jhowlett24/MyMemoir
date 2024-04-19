package com.example.mymemoir1

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ViewNotesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)

        firestore = FirebaseFirestore.getInstance()
        val listViewNotes: ListView = findViewById(R.id.listViewNotes)
        progressBar = findViewById(R.id.progressBar)

        fetchNotesFromFirestore(listViewNotes)
    }

    private fun fetchNotesFromFirestore(listViewNotes: ListView) {
        progressBar.visibility = View.VISIBLE  // Show the progress bar
        val notesCollection = firestore.collection("notes")

        notesCollection.get()
            .addOnSuccessListener { documents ->
                progressBar.visibility = View.GONE  // Hide the progress bar
                val notesList = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val content = document.getString("content") ?: ""
                    val unlockTime = document.getLong("unlockTime") ?: 0
                    if (System.currentTimeMillis() >= unlockTime) {
                        notesList.add("$title: $content")
                    } else {
                        val timeLeft = unlockTime - System.currentTimeMillis()
                        val displayTimeLeft = formatTimeLeft(timeLeft)
                        notesList.add("$title - Time until note is accessible: $displayTimeLeft")
                    }
                }
                val adapter = ArrayAdapter(this@ViewNotesActivity, android.R.layout.simple_list_item_1, notesList)
                listViewNotes.adapter = adapter
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE  // Hide the progress bar on failure
                Toast.makeText(this@ViewNotesActivity, "Error fetching notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun formatTimeLeft(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60) % 60)
        val hours = (millis / (1000 * 60 * 60) % 24)
        val days = millis / (1000 * 60 * 60 * 24)
        return String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
    }
}
