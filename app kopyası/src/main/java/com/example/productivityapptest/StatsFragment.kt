package com.example.productivityapptest

import SharedViewModel
import com.example.productivityapptest.DailyStats
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StatsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statistics, container, false)

        val statsRecyclerView: RecyclerView = view.findViewById(R.id.statsRecyclerView)
        statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val statsAdapter = StatsAdapter()
        statsRecyclerView.adapter = statsAdapter

        sharedViewModel.dailyStatistics.observe(viewLifecycleOwner) { statsMap ->
            statsAdapter.submitList(statsMap.values.toList())
        }

        sharedViewModel.fetchTasksFromFirestore()

        return view
    }
}

class StatsAdapter :
    RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

    private val statsList = mutableListOf<DailyStats>()

    fun submitList(newList: List<DailyStats>) {
        statsList.clear()
        statsList.addAll(newList)
        notifyDataSetChanged()
    }

    class StatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val totalTasksTextView: TextView = itemView.findViewById(R.id.totalTasksTextView)
        private val completedTasksTextView: TextView = itemView.findViewById(R.id.completedTasksTextView)

        fun bind(dailyStats: DailyStats) {
            dateTextView.text = "Date: ${dailyStats.date}"
            totalTasksTextView.text = "Total Tasks: ${dailyStats.totalTasks}"
            completedTasksTextView.text = "Completed: ${dailyStats.completedTasks}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_statistics, parent, false)
        return StatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.bind(statsList[position])
    }

    override fun getItemCount(): Int = statsList.size
}