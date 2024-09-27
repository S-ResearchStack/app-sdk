package researchstack.data.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "passive_data_status",
    indices = [Index(value = ["enabled"])]
)
data class PassiveDataStatusEntity(
    @PrimaryKey val dataType: String,
    val enabled: Boolean = false,
)
