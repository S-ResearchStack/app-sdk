package researchstack.domain.model.events

import researchstack.domain.model.Timestamp
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

data class MobileWearConnection(
    override val timestamp: Long = LocalDateTime.now().toEpochMilli(),
    override val timeOffset: Int = getCurrentTimeOffset(),
    val wearableDeviceName: String = "",
) : Timestamp
