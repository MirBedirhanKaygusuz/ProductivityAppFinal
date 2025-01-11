package com.example.productivityapptest

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditTaskFragment : Fragment() {
    private val args: EditTaskFragmentArgs by navArgs()
    private lateinit var firestore: FirebaseFirestore

    private var selectedDate: String? = null
    private var selectedTime: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_edit_task, container, false)

        val editTaskTitle: EditText = view.findViewById(R.id.editTaskTitle)
        val editTaskDescription: EditText = view.findViewById(R.id.editTaskDescription)
        val selectedDateTextView: TextView = view.findViewById(R.id.selectedDateTextView)
        val selectDateButton: Button = view.findViewById(R.id.selectDateButton)
        val selectedTimeTextView: TextView = view.findViewById(R.id.selectedTimeTextView)
        val selectTimeButton: Button = view.findViewById(R.id.selectTimeButton)
        val saveTaskButton: Button = view.findViewById(R.id.saveTaskButton)

        firestore = FirebaseFirestore.getInstance()

        // Varsayılan değerleri yükle
        editTaskTitle.setText(args.taskName)
        editTaskDescription.setText(args.taskDescription)

        // Tarih seçimi
        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                selectedDate = "$dayOfMonth/${month + 1}/$year"
                selectedDateTextView.text = "Selected Date: $selectedDate"
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Saat seçimi
        selectTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                selectedTimeTextView.text = "Selected Time: $selectedTime"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        // Görevi kaydet
        saveTaskButton.setOnClickListener {
            val updatedTitle = editTaskTitle.text.toString().trim()
            val updatedDescription = editTaskDescription.text.toString().trim()

            if (updatedTitle.isNotEmpty()) {
                updateTaskInFirestore(args.taskName, updatedTitle, updatedDescription, selectedDate, selectedTime)
            } else {
                Toast.makeText(requireContext(), "Task title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateTaskInFirestore(
        originalTaskName: String,
        updatedTitle: String,
        updatedDescription: String,
        updatedDate: String?,
        updatedTime: String?
    ) {
        firestore.collection("tasks")
            .whereEqualTo("title", originalTaskName)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        firestore.collection("tasks").document(document.id)
                            .update(
                                mapOf(
                                    "title" to updatedTitle,
                                    "description" to updatedDescription,
                                    "date" to updatedDate,
                                    "time" to updatedTime
                                )
                            )
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Task updated successfully", Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Error updating task: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(requireContext(), "Task not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error fetching task: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}