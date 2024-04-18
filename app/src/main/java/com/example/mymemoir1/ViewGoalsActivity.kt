package com.example.mymemoir1
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class ViewGoalsActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    //private lateinit var goalsList: MutableList<String>
    //private lateinit var sharedPreferences: SharedPreferences
    //added line
    private lateinit var listViewGoals: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_goals)

        //val listViewGoals: ListView = findViewById(R.id.listViewGoals)
        listViewGoals = findViewById(R.id.listViewGoals)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        //sharedPreferences = getSharedPreferences("GoalsPrefs", MODE_PRIVATE)
        //loadGoals()

        listViewGoals.adapter = adapter
        fetchGoalsFromFirestore()
    }
    //added new function
    private fun fetchGoalsFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val goalsCollection = firestore.collection("goals")

        goalsCollection.get()
            .addOnSuccessListener { documents ->
                val goalsList = mutableListOf<String>()
                for (document in documents) {
                    val title = document.getString("title") ?: ""
                    val description = document.getString("description") ?: ""
                    val stepsGoal = document.getString("stepsGoal") ?: ""
                    val goalString = "$title: $description - $stepsGoal"
                    goalsList.add(goalString)
                }
                adapter.addAll(goalsList)
            }
            .addOnFailureListener { e ->
                // Handle failures
                e.printStackTrace()
            }
    }

    /*
        listViewGoals.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            val goal = goalsList[position]
            val goalKey = goal.substringBefore(":") // Assuming goal is stored as "Title: Step Count"


            AlertDialog.Builder(this)
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal?")
                .setPositiveButton("Yes") { dialog, _ ->
                    deleteGoal(goalKey)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                .show()

            true
        }
    }

    private fun loadGoals() {
        val goalsMap: Map<String, *> = sharedPreferences.all
        goalsList = goalsMap.entries.map { (key, value) -> "$key: $value" }.toMutableList()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, goalsList)
    }

    private fun deleteGoal(goalKey: String) {
        // Remove the goal from SharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove(goalKey)
        editor.apply()

        // Update the goals list and notify the adapter
        goalsList.removeAll { it.startsWith(goalKey) }
        adapter.notifyDataSetChanged()

        Toast.makeText(this, "Goal deleted", Toast.LENGTH_SHORT).show()
    }
*/
}
