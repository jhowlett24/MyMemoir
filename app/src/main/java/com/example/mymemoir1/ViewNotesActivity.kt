package com.example.mymemoir1

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ViewNotesActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)

        firestore = FirebaseFirestore.getInstance()
        val listViewNotes: ListView = findViewById(R.id.listViewNotes)

        fetchNotesFromFirestore(listViewNotes)
    }

    private fun fetchNotesFromFirestore(listViewNotes: ListView) {
        val notesCollection = firestore.collection("notes")

        notesCollection.get()
            .addOnSuccessListener { documents ->
                val notesList = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val content = document.getString("content") ?: ""
                    val timeDuration = document.getString("timeDuration") ?: ""
                    val noteString = "$title: $content ($timeDuration)"
                    notesList.add(noteString)
                }
                val adapter = ArrayAdapter(this@ViewNotesActivity, android.R.layout.simple_list_item_1, notesList)
                listViewNotes.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@ViewNotesActivity, "Error fetching notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
