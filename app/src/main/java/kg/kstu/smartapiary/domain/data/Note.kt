package kg.kstu.smartapiary.domain.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String,
    val text: String,
    val date: String,
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
