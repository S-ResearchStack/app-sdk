package researchstack.model.events

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.WearableBattery
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

class WearableBatteryTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `test WearableBattery default values`() {
        val timeStamp = LocalDateTime.now()
        val currentTime = timeStamp.toEpochMilli()
        val timeOffset = getCurrentTimeOffset()
        val percentage = 50L
        val isCharging = 1

        val wearableBattery = WearableBattery(currentTime, timeOffset, percentage, isCharging)

        val currentTimeExpected = timeStamp.toEpochMilli()
        val timeOffsetExpected = getCurrentTimeOffset()
        assertEquals(currentTimeExpected, wearableBattery.timestamp)
        assertEquals(timeOffsetExpected, wearableBattery.timeOffset)
        assertEquals(50L, wearableBattery.percentage)
        assertEquals(1, wearableBattery.isCharging)
    }
}
