package healthstack.common.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import healthstack.common.util.getCurrentTimeOffset

const val HEART_RATE_TABLE_NAME = "heartrate"

@Entity(
    tableName = HEART_RATE_TABLE_NAME
)
data class HeartRate(
    @PrimaryKey override val timestamp: Long = 0,
    val value: Int = 0,
    val ibiList: List<Int> = emptyList(),
    val ibiStatusList: List<Int> = emptyList(),
    val heartRateStatus: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : WearData {
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
