package com.example.mymemoir1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore


class SetGoalsActivity : AppCompatActivity() {
    //added
    private lateinit var firestore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        //added line
        firestore = FirebaseFirestore.getInstance()

        val stepsOptions = arrayOf("20,000 steps", "40,000 steps", "60,000 steps", "80,000 steps",
            "100,000 steps", "250,000 steps", "500,000 steps", "1,000,000 steps")
        val spinnerGoalSteps: Spinner = findViewById(R.id.spinnerGoalSteps)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stepsOptions)
        spinnerGoalSteps.adapter = adapter

        val editTextGoalTitle: EditText = findViewById(R.id.editTextGoalTitle)
        val editTextGoalDescription: EditText = findViewById(R.id.editTextGoalDescription)
        val buttonSaveGoal: Button = findViewById(R.id.buttonSaveGoal)

        buttonSaveGoal.setOnClickListener {
            val goalTitle = editTextGoalTitle.text.toString()
            val goalDescription = editTextGoalDescription.text.toString()
            val selectedStepGoal = spinnerGoalSteps.selectedItem.toString()

            //added line
            val goalsCollection = firestore.collection("goals")

            //added lines

            val newGoalDocument = goalsCollection.document()
            val goalData = hashMapOf(
                "title" to goalTitle,
                "description" to goalDescription,
                "stepsGoal" to selectedStepGoal
            )
            newGoalDocument.set(goalData)
                .addOnSuccessListener {
                    // Document successfully written
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    // Handle errors
                    Toast.makeText(this, "Error saving goal: ${e.message}", Toast.LENGTH_SHORT).show()
                }



         /*   val sharedPreferences = getSharedPreferences("GoalsPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(goalTitle, selectedStepGoal)
            editor.apply()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
*/
            // Optionally, navigate back to the ViewGoalsActivity or show a confirmation message
        }
    }
}
