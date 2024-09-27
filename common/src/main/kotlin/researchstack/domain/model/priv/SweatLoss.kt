package researchstack.domain.model.priv

import androidx.room.Entity
import androidx.room.PrimaryKey
import researchstack.domain.model.TimestampMapData
import researchstack.util.getCurrentTimeOffset

const val SWEAT_LOSS_TABLE_NAME = "sweat_loss"

@Entity(
    tableName = SWEAT_LOSS_TABLE_NAME
)
data class SweatLoss(
    @PrimaryKey
    override val timestamp: Long = 0,
    val sweatLoss: Float = 0f,
    val status: Int = 0,
    override val timeOffset: Int = getCurrentTimeOffset(),
) : TimestampMapData {
    override fun toDataMap(): Map<String, Any> =
        mapOf(
            ::timestamp.name to timestamp,
            ::sweatLoss.name to sweatLoss,
            ::status.name to status,
            ::timeOffset.name to timeOffset,
        )
}
