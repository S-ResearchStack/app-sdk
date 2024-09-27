package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val PPG_GREEN_TABLE_NAME = "ppg_green"

const val PPG_IR_TABLE_NAME = "ppg_ir"

const val PPG_RED_TABLE_NAME = "ppg_red"

interface PPGAttribute {
    val ppg: Int
}

sealed class Ppg : PPGAttribute

@Entity(
    tableName = PPG_GREEN_TABLE_NAME
)
data class PpgGreen(
    @PrimaryKey override val timestamp: Long = 0,
    override val ppg: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : Ppg(), TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::ppg.name to ppg,
            ::timeOffset.name to timeOffset,
        )
}

@Entity(
    tableName = PPG_IR_TABLE_NAME
)
data class PpgIr(
    @PrimaryKey override val timestamp: Long = 0,
    override val ppg: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : Ppg(), TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::ppg.name to ppg,
            ::timeOffset.name to timeOffset,
        )
}

@Entity(
    tableName = PPG_RED_TABLE_NAME
)
data class PpgRed(
    @PrimaryKey override val timestamp: Long = 0,
    override val ppg: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : Ppg(), TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::ppg.name to ppg,
            ::timeOffset.name to timeOffset,
        )
}
