package researchstack.domain.model.sensor

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

const val ACCELEROMETER_TABLE_NAME = "accelerometer"

@Entity(
    tableName = ACCELEROMETER_TABLE_NAME
)
data class Accelerometer(
    val x: Float,
    val y: Float,
    val z: Float,
    @PrimaryKey override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> = buildMap {
        put(::x.name, x)
        put(::y.name, y)
        put(::z.name, z)
        putAll(super.toTimeDataMap())
    }
}
