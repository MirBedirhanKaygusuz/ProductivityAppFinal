import com.google.firebase.firestore.PropertyName

data class Task(
    val title: String = "",
    val description: String = "",
    @get:PropertyName("completed") @set:PropertyName("completed")
    var isCompleted: Boolean = false, // Firestore'daki 'completed' ile eşleştir
    val date: String? = null,
    val time: String? = null,
    var documentId: String = "" // Belge kimliği
)