package researchstack.domain.model.sensor

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

const val LIGHT_TABLE_NAME = "light"

@Entity(
    tableName = LIGHT_TABLE_NAME
)
data class Light(
    val accuracy: Int,
    val lx: Int,
    @PrimaryKey override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> = buildMap {
        put(::accuracy.name, accuracy.toDouble())
        put(::lx.name, lx.toDouble())
        putAll(super.toTimeDataMap())
    }
}
