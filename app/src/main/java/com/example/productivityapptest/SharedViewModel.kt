import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import com.example.productivityapptest.DailyStats

class SharedViewModel : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _dailyStatistics = MutableLiveData<Map<String, DailyStats>>()
    val dailyStatistics: LiveData<Map<String, DailyStats>> get() = _dailyStatistics

    private val db = FirebaseFirestore.getInstance()

    fun fetchTasksFromFirestore() {
        db.collection("tasks").get()
            .addOnSuccessListener { result ->
                val taskList = result.map { document ->
                    document.toObject(Task::class.java).apply {
                        documentId = document.id
                    }
                }
                _tasks.value = taskList
                calculateDailyStatistics(taskList)
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error fetching tasks", e)
            }
    }

    fun deleteTaskFromFirestore(taskId: String) {
        db.collection("tasks").document(taskId)
            .delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Task deleted successfully.")
                fetchTasksFromFirestore() // Listeyi yenile
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting task", e)
            }
    }

    private fun calculateDailyStatistics(tasks: List<Task>) {
        val statsMap = mutableMapOf<String, DailyStats>()

        tasks.forEach { task ->
            val date = task.date ?: "No Date"
            val dailyStats = statsMap.getOrPut(date) { DailyStats(date) }
            dailyStats.totalTasks++
            if (task.isCompleted) dailyStats.completedTasks++
        }

        _dailyStatistics.value = statsMap
    }
}