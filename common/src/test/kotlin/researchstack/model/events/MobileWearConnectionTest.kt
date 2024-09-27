package researchstack.model.events

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.events.MobileWearConnection
import researchstack.util.getCurrentTimeOffset
import researchstack.util.toEpochMilli
import java.time.LocalDateTime

class MobileWearConnectionTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `test MobileWearConnection default values`() {
        val timeStamp = LocalDateTime.now()
        val mobileWearConnection = MobileWearConnection(timeStamp.toEpochMilli())

        val currentTime = timeStamp.toEpochMilli()
        val timeOffset = getCurrentTimeOffset()
        assertEquals(currentTime, mobileWearConnection.timestamp)
        assertEquals(timeOffset, mobileWearConnection.timeOffset)
        assertEquals("", mobileWearConnection.wearableDeviceName)
    }
}
