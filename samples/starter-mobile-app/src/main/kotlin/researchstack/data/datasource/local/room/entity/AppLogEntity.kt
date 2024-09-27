package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logs")
data class AppLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val timeStamp: Long,
    val data: Map<String, String> = mutableMapOf(),
    val sent: Boolean = false,
)
