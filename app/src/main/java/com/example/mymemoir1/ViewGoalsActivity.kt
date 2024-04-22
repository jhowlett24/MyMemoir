package com.example.mymemoir1

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import android.text.InputType

class ViewGoalsActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private lateinit var listViewGoals: ListView
    private lateinit var buttonManualEntry: Button

    companion object {
        const val REQUEST_ACTIVITY_RECOGNITION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_goals)

        firestore = FirebaseFirestore.getInstance()
        listViewGoals = findViewById(R.id.listViewGoals)
        buttonManualEntry = findViewById(R.id.buttonManualEntry)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        checkPermissionsAndInitializeSensor()
        fetchGoals()
    }

    private fun checkPermissionsAndInitializeSensor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), REQUEST_ACTIVITY_RECOGNITION)
            } else {
                initializeSensor()
            }
        } else {
            initializeSensor()
        }
    }

    private fun initializeSensor() {
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
        } else {
            buttonManualEntry.visibility = Button.VISIBLE
            buttonManualEntry.setOnClickListener { showManualEntryDialog() }
            Toast.makeText(this, "Step Counter Sensor not available! Using manual entry.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchGoals() {
        firestore.collection("goals").get()
            .addOnSuccessListener { documents ->
                val goalsList = documents.mapNotNull { document ->
                    val title = document.getString("title") ?: "No Title"
                    val description = document.getString("description") ?: "No Description"
                    val stepsGoal = document.data["stepsGoal"]?.toString()?.toLongOrNull() ?: 0L
                    val stepsCompleted = document.data["stepsCompleted"]?.toString()?.toLongOrNull() ?: 0L

                    // Conditionally display the description based on the steps goal
                    if (stepsCompleted >= stepsGoal) {
                        "\nGoal of $stepsGoal completed!\n$title: \n\n$description\n"
                    } else {
                        val stepsLeft = stepsGoal - stepsCompleted
                        "\n$title: \n\nGoal: $stepsGoal steps\nCompleted: $stepsCompleted steps\nSteps left: $stepsLeft\n"
                    }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, goalsList)
                listViewGoals.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching goals: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }



    private fun showManualEntryDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enter Steps Manually")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val stepsEntered = input.text.toString().toIntOrNull() ?: 0
            updateStepsManually(stepsEntered)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun updateStepsManually(steps: Int) {
        firestore.collection("goals").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val currentSteps = document.getLong("stepsCompleted") ?: 0
                    val newSteps = currentSteps + steps
                    document.reference.update("stepsCompleted", newSteps)
                        .addOnCompleteListener {
                            fetchGoals() // Refetch all goals after updates to refresh the list
                        }
                }
            }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val steps = it.values[0].toInt()
                updateStepsManually(steps)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this context
    }

    override fun onDestroy() {
        super.onDestroy()
        stepCounterSensor?.let { sensorManager.unregisterListener(this) }
    }
}
