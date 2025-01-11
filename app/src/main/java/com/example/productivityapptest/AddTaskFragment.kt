package com.example.productivityapptest

import Task
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTaskFragment : Fragment(R.layout.fragment_add_task) {

    private lateinit var firestore: FirebaseFirestore
    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        val taskInput: EditText = view.findViewById(R.id.taskInput)
        val descriptionInput: EditText = view.findViewById(R.id.descriptionInput)
        val selectDateButton: Button = view.findViewById(R.id.selectDateButton)
        val selectTimeButton: Button = view.findViewById(R.id.selectTimeButton)
        val confirmTaskButton: FloatingActionButton = view.findViewById(R.id.confirmTaskButton)

        firestore = FirebaseFirestore.getInstance()

        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                    selectDateButton.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        selectTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    selectedTime = "$hourOfDay:$minute"
                    selectTimeButton.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        confirmTaskButton.setOnClickListener {
            val taskTitle = taskInput.text.toString().trim()
            val taskDescription = descriptionInput.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                saveTaskToFirestore(taskTitle, taskDescription, selectedDate, selectedTime)
            } else {
                Toast.makeText(requireContext(), "Please enter a task title.", Toast.LENGTH_SHORT).show()
            }
        }



        return view
    }

    private fun saveTaskToFirestore(title: String, description: String, date: String?, time: String?) {
        val task = Task(title, description, isCompleted = false, date = date, time = time)

        firestore.collection("tasks")
            .add(task)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Task saved successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}