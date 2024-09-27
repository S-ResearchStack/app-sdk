package researchstack.domain.model.events

import researchstack.domain.model.Timestamp
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

data class WearablePowerState(
    override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
    val powerState: Int = 0,
) : Timestamp
