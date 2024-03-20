package com.example.mymemoir1

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SetGoalsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_goals)

        val stepsOptions = arrayOf("20,000 steps", "40,000 steps", "60,000 steps", "80,000 steps", "100,000 steps", "250,000 steps", "500,000 steps", "1,000,000 steps")
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

            val sharedPreferences = getSharedPreferences("GoalsPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(goalTitle, selectedStepGoal)
            editor.apply()

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // Optionally, navigate back to the ViewGoalsActivity or show a confirmation message fsdafa
        }
    }
}
