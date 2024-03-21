package com.example.mymemoir1
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_notes)

        val listViewNotes: ListView = findViewById(R.id.listViewNotes)
        val sharedPreferences = getSharedPreferences("NotesPrefs", MODE_PRIVATE)
        val notesMap: Map<String, *> = sharedPreferences.all

        val notesList = notesMap.map { (key, value) -> "$key: $value" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        listViewNotes.adapter = adapter
    }
}
