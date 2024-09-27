package researchstack.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.Accelerometer
import researchstack.util.getCurrentTimeOffset

class AccelerometerTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `Accelerometer toDataMap should work fine`() {
        val timestamp = 12341234L
        val x = 1
        val y = 2
        val z = 3
        val timeOffset = getCurrentTimeOffset()
        val expectedMap = mapOf(
            "timestamp" to timestamp,
            "x" to x,
            "y" to y,
            "z" to z,
            "timeOffset" to timeOffset,
        )

        val accelerometer = Accelerometer(timestamp, x, y, z, timeOffset)
        val dataMap = accelerometer.toDataMap()

        assertEquals(expectedMap, dataMap)
    }
}
