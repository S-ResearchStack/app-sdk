package researchstack.domain.model.events

import researchstack.domain.model.Timestamp
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

data class WearableBattery(
    override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
    val percentage: Long,
    val isCharging: Int
) : Timestamp
