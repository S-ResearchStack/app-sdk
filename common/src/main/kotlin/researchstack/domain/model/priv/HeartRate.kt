package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val HEART_TABLE_NAME = "heartrate"

@Entity(
    tableName = HEART_TABLE_NAME
)
data class HeartRate(
    @PrimaryKey override val timestamp: Long = 0,
    val value: Int = 0,
    val ibiList: List<Int> = emptyList(),
    val ibiStatusList: List<Int> = emptyList(),
    val heartRateStatus: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::ibiList.name to ibiList,
            ::ibiStatusList.name to ibiStatusList,
            ::heartRateStatus.name to heartRateStatus,
            ::value.name to value,
            ::timeOffset.name to timeOffset,
        )
}
