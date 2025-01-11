package com.example.productivityapptest

import SharedViewModel
import TaskAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        val taskRecyclerView: RecyclerView = view.findViewById(R.id.taskRecyclerView)
        taskAdapter = TaskAdapter { task ->
            val bundle = Bundle().apply {
                putString("taskName", task.title)
                putString("taskDescription", task.description)
            }
            findNavController().navigate(R.id.taskDetailFragment, bundle)
        }
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskRecyclerView.adapter = taskAdapter

        val addTaskButton: FloatingActionButton = view.findViewById(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddTaskFragment()
            findNavController().navigate(action)
        }

        sharedViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            tasks.forEach { task ->
                Log.d("Firestore Task", "Title: ${task.title}, Date: ${task.date}, Time: ${task.time}")
            }
            taskAdapter.submitList(tasks)
        }

        sharedViewModel.fetchTasksFromFirestore()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "List"
    }
}