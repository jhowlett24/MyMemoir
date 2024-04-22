package com.example.mymemoir1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SetGoalsActivity : AppCompatActivity() {
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        firestore = FirebaseFirestore.getInstance()
        val stepsOptions = arrayOf("20000", "40000", "60000", "80000", "100000", "250000", "500000", "1000000")
        val spinnerGoalSteps: Spinner = findViewById(R.id.spinnerGoalSteps)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stepsOptions.map { "$it steps" })
        spinnerGoalSteps.adapter = adapter

        val editTextGoalTitle: EditText = findViewById(R.id.editTextGoalTitle)
        val editTextGoalDescription: EditText = findViewById(R.id.editTextGoalDescription)
        val buttonSaveGoal: Button = findViewById(R.id.buttonSaveGoal)

        buttonSaveGoal.setOnClickListener {
            val goalTitle = editTextGoalTitle.text.toString().trim()
            val goalDescription = editTextGoalDescription.text.toString().trim()
            val selectedStepGoal = spinnerGoalSteps.selectedItem.toString().filter { it.isDigit() }.toInt()

            if (goalTitle.isNotEmpty() && goalDescription.isNotEmpty() && selectedStepGoal > 0) {
                val goalData = hashMapOf(
                    "title" to goalTitle,
                    "description" to goalDescription,
                    "stepsGoal" to selectedStepGoal,
                    "stepsCompleted" to 0
                )
                firestore.collection("goals").add(goalData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Goal saved successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving goal: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please ensure all fields are filled correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
