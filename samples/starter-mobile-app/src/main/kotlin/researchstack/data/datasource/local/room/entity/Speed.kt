package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

const val SPEED_TABLE_NAME = "speed"

@Entity(
    tableName = SPEED_TABLE_NAME
)
data class Speed(
    val velocity: Float,
    val fromTime: Long,
    val endTime: Long,
    @PrimaryKey override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        buildMap {
            put(::velocity.name, velocity)
            put(::fromTime.name, fromTime)
            put(::endTime.name, endTime)
            putAll(super.toTimeDataMap())
        }
}
