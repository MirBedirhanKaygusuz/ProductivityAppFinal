package com.example.productivityapptest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore

class TaskDetailFragment : Fragment() {
    private val args: TaskDetailFragmentArgs by navArgs()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_task_detail, container, false)

        val taskNameTextView: TextView = view.findViewById(R.id.taskNameTextView)
        val taskDescriptionTextView: TextView = view.findViewById(R.id.taskDescriptionTextView)
        val editTaskButton: Button = view.findViewById(R.id.editTaskButton)
        val deleteTaskButton: Button = view.findViewById(R.id.deleteTaskButton)

        firestore = FirebaseFirestore.getInstance()

        val taskName = args.taskName
        val taskDescription = args.taskDescription

        taskNameTextView.text = taskName
        taskDescriptionTextView.text = taskDescription

        // Edit button click event
        editTaskButton.setOnClickListener {
            val action = TaskDetailFragmentDirections.actionTaskDetailFragmentToEditTaskFragment(
                taskName,
                taskDescription
            )
            findNavController().navigate(action)
        }

        // Delete button click event
        deleteTaskButton.setOnClickListener {
            deleteTaskFromFirestore(taskName)
        }

        return view
    }

    private fun deleteTaskFromFirestore(taskName: String) {
        firestore.collection("tasks")
            .whereEqualTo("title", taskName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(requireContext(), "Task not found", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in documents) {
                        firestore.collection("tasks").document(document.id).delete()
                            .addOnSuccessListener {
                                Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Failed to delete task", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error fetching task", Toast.LENGTH_SHORT).show()
            }
    }
}