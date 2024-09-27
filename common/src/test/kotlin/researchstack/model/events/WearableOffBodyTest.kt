package researchstack.model.events

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.WearableOffBody
import researchstack.util.getCurrentTimeOffset

class WearableOffBodyTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun testTimestampProperty() {
        val timestamp = 1633072800000L
        val timeOffset = getCurrentTimeOffset()
        val isWearableOffBody = 0

        val wearableOffBody = WearableOffBody(timestamp, timeOffset, isWearableOffBody)

        assertEquals(timestamp, wearableOffBody.timestamp)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testTimeOffsetProperty() {
        val timestamp = System.currentTimeMillis()
        val timeOffset = 540
        val isWearableOffBody = 0

        val wearableOffBody = WearableOffBody(timestamp, timeOffset, isWearableOffBody)

        assertEquals(timeOffset, wearableOffBody.timeOffset)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun testIsWearableOffBodyProperty() {
        val timestamp = System.currentTimeMillis()
        val timeOffset = getCurrentTimeOffset()
        val isWearableOffBody = 1

        val wearableOffBody = WearableOffBody(timestamp, timeOffset, isWearableOffBody)

        assertEquals(isWearableOffBody, wearableOffBody.isWearableOffBody)
    }
}
