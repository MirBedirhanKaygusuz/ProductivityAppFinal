import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.productivityapptest.DailyStats
import com.example.productivityapptest.R

class StatsAdapter : RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {

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