package com.example.productivityapptest

import Task
import TaskAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CalendarFragment : Fragment() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        val calendarView: CalendarView = view.findViewById(R.id.calendarView)
        val selectedDateText: TextView = view.findViewById(R.id.selectedDateText)
        val taskRecyclerView: RecyclerView = view.findViewById(R.id.taskRecyclerView)

        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // TaskAdapter onClick ile başlatılır
        taskAdapter = TaskAdapter { task ->
            updateTaskCompletion(task.documentId, !task.isCompleted) // Tamamlanma durumunu değiştir
        }

        taskRecyclerView.adapter = taskAdapter

        firestore = FirebaseFirestore.getInstance()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            selectedDateText.text = "Tasks for: $selectedDate"
            fetchTasksForDate(selectedDate)
        }

        return view
    }

    private fun fetchTasksForDate(date: String) {
        firestore.collection("tasks")
            .whereEqualTo("date", date)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val tasks = querySnapshot.documents.mapNotNull { it.toObject(Task::class.java)
                    ?.apply { documentId = it.id } }
                taskAdapter.submitList(tasks)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching tasks for date: $date", exception)
            }
    }

    private fun updateTaskCompletion(taskId: String, isCompleted: Boolean) {
        firestore.collection("tasks").document(taskId)
            .update("completed", isCompleted)
            .addOnSuccessListener {
                Log.d("Firestore", "Task completion updated to $isCompleted")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error updating task completion", e)
            }
    }
}