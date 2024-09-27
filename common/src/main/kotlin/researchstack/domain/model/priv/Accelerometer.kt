package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val ACCELEROMETER_TABLE_NAME = "accelerometer"

@Entity(
    tableName = ACCELEROMETER_TABLE_NAME
)
data class Accelerometer(
    @PrimaryKey
    override val timestamp: Long = 0,
    val x: Int = 0,
    val y: Int = 0,
    val z: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::x.name to x,
            ::y.name to y,
            ::z.name to z,
            ::timeOffset.name to timeOffset,
        )
}
