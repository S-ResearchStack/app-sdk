package researchstack.domain.model.events

import researchstack.domain.model.Timestamp
import researchstack.util.getCurrentTimeOffset

data class WearableOffBody(
    override val timestamp: Long,
    override val timeOffset: Int = getCurrentTimeOffset(),
    val isWearableOffBody: Int
) : Timestamp
