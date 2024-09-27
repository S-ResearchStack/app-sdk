package researchstack.model.events

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.WearablePowerState
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

class WearablePowerStateTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun testDefaultConstructor() {
        val now = LocalDateTime.now()
        val expectedTimestamp = now.toEpochMilli()
        val expectedTimeOffset = getCurrentTimeOffset()

        val wearablePowerState = WearablePowerState()

        assertEquals(expectedTimestamp, wearablePowerState.timestamp)
        assertEquals(expectedTimeOffset, wearablePowerState.timeOffset)
        assertEquals(0, wearablePowerState.powerState)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testCustomConstructor() {
        val timestamp = 1633072800000L
        val timeOffset = 540
        val powerState = 1

        val wearablePowerState = WearablePowerState(timestamp, timeOffset, powerState)

        assertEquals(timestamp, wearablePowerState.timestamp)
        assertEquals(timeOffset, wearablePowerState.timeOffset)
        assertEquals(powerState, wearablePowerState.powerState)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testTimestampProperty() {
        val timestamp = 1633072800000L
        val timeOffset = getCurrentTimeOffset()
        val powerState = 0

        val wearablePowerState = WearablePowerState(timestamp, timeOffset, powerState)

        assertEquals(timestamp, wearablePowerState.timestamp)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testTimeOffsetProperty() {
        val timestamp = LocalDateTime.now().toEpochMilli()
        val timeOffset = 540
        val powerState = 0

        val wearablePowerState = WearablePowerState(timestamp, timeOffset, powerState)

        assertEquals(timeOffset, wearablePowerState.timeOffset)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testPowerStateProperty() {
        val timestamp = LocalDateTime.now().toEpochMilli()
        val timeOffset = getCurrentTimeOffset()
        val powerState = 1

        val wearablePowerState = WearablePowerState(timestamp, timeOffset, powerState)

        assertEquals(powerState, wearablePowerState.powerState)
    }
}
