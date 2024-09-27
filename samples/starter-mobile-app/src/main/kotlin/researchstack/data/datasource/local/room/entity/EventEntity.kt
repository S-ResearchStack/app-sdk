package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey
    val timestamp: Long,
    val studyId: String,
    val name: String,
    val extras: Map<String, String>,
)
