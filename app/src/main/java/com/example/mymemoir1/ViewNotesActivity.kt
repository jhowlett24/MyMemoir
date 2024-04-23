package com.example.mymemoir1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var listViewNotes: ListView
    private var notesList = mutableListOf<String>()
    private var documentsList = mutableListOf<Map<String, Any>>()

    private val updateHandler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            updateNoteTimes()
            updateHandler.postDelayed(this, 1000) // Schedule this again in one second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)

        firestore = FirebaseFirestore.getInstance()
        listViewNotes = findViewById(R.id.listViewNotes)
        progressBar = findViewById(R.id.progressBar)

        fetchNotesFromFirestore()
    }

    private fun fetchNotesFromFirestore() {
        progressBar.visibility = View.VISIBLE  // Show the progress bar
        val notesCollection = firestore.collection("notes")

        notesCollection.get()
            .addOnSuccessListener { documents ->
                progressBar.visibility = View.GONE  // Hide the progress bar
                documentsList.clear()
                documents.forEach { document ->
                    val data = document.data
                    data["id"] = document.id  // Save document ID for potential future use
                    documentsList.add(data)
                }
                updateNoteTimes()  // Initial update
                updateHandler.postDelayed(updateRunnable, 1000)  // Start the periodic update
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE  // Hide the progress bar on failure
                Toast.makeText(this@ViewNotesActivity, "Error fetching notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateNoteTimes() {
        notesList.clear()
        val currentTime = System.currentTimeMillis()
        documentsList.forEach { document ->
            val title = document["title"] as? String ?: ""
            val content = document["content"] as? String ?: ""
            val unlockTime = document["unlockTime"] as? Long ?: 0
            if (currentTime >= unlockTime) {
                notesList.add("\n$title: \n\n$content\n")
            } else {
                val timeLeft = unlockTime - currentTime
                val displayTimeLeft = formatTimeLeft(timeLeft)
                notesList.add("\n$title:\n\n$displayTimeLeft\n")
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        listViewNotes.adapter = adapter
    }

    private fun formatTimeLeft(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60) % 60)
        val hours = (millis / (1000 * 60 * 60) % 24)
        val days = millis / (1000 * 60 * 60 * 24)
        return String.format("%d days, %02d:%02d:%02d", days, hours, minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        updateHandler.removeCallbacks(updateRunnable) // Stop the handler when the activity is destroyed to prevent memory leaks
    }
}
