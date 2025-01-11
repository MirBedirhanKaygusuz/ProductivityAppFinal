import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.productivityapptest.R

class TaskAdapter(private val onClick: (Task) -> Unit) :
    ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(itemView: View, private val onClick: (Task) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val taskCheckBox: CheckBox = itemView.findViewById(R.id.taskCheckBox)
        private val taskTitleTextView: TextView = itemView.findViewById(R.id.taskTitleTextView)
        private val taskDateTextView: TextView = itemView.findViewById(R.id.taskDateTextView)
        private val taskTimeTextView: TextView = itemView.findViewById(R.id.taskTimeTextView)

        private lateinit var currentTask: Task
        private val firestore = FirebaseFirestore.getInstance()

        init {
            itemView.setOnClickListener {
                onClick(currentTask)
            }

            taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (::currentTask.isInitialized && currentTask.documentId.isNotEmpty()) {
                    firestore.collection("tasks")
                        .document(currentTask.documentId)
                        .update("completed", isChecked)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Task completion status updated to $isChecked")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error updating task completion status", e)
                        }
                }
            }
        }

        fun bind(task: Task) {
            currentTask = task
            taskTitleTextView.text = task.title
            taskCheckBox.isChecked = task.isCompleted
            taskDateTextView.text = task.date ?: "No Date"
            taskTimeTextView.text = task.time ?: "No Time"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.documentId == newItem.documentId
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}