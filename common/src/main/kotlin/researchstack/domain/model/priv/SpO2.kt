package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val SPO2_TABLE_NAME = "spO2"

@Entity(
    tableName = SPO2_TABLE_NAME
)
data class SpO2(
    @PrimaryKey
    override val timestamp: Long = 0,
    val heartRate: Int = 0,
    val spO2: Int = 0,
    val status: Flag = Flag.FAILED,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    var sessionId: Long = 0
    enum class Flag {
        LOW_SIGNAL,
        DEVICE_MOVING,
        INITIAL_STATUS,
        CALCULATING,
        MEASUREMENT_COMPLETED,
        FAILED
    }
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::heartRate.name to heartRate,
            ::spO2.name to spO2,
            ::status.name to status.ordinal,
            ::timeOffset.name to timeOffset,
        )
}
