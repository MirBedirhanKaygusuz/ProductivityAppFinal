package com.example.productivityapptest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TaskDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_task_detail, container, false)

        val taskNameTextView: TextView = view.findViewById(R.id.taskNameTextView)
        val taskDescriptionTextView: TextView = view.findViewById(R.id.taskDescriptionTextView)

        // Argümanları Bundle'dan al
        val taskName = arguments?.getString("taskName") ?: "No Task Name"
        val taskDescription = arguments?.getString("taskDescription") ?: "No Description"

        taskNameTextView.text = taskName
        taskDescriptionTextView.text = taskDescription

        return view
    }
}