package researchstack.model

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.util.getCurrentTimeOffset
import java.time.Instant

class PpgTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `test toDataMap method for PpgGreen`() {
        val timestamp = 12341234L
        val ppg = 12345
        val timeOffset = getCurrentTimeOffset()
        val expectedDataMap = mapOf(
            "timestamp" to timestamp,
            "ppg" to ppg,
            "timeOffset" to timeOffset
        )

        val ppgGreen = PpgGreen(timestamp, ppg)
        val dataMap = ppgGreen.toDataMap()

        assertEquals(expectedDataMap, dataMap)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `test toDataMap method for PpgIr`() {
        val currentTime = Instant.now().toEpochMilli()
        val ppg = 67890
        val timeOffset = getCurrentTimeOffset()
        val expectedDataMap = mapOf(
            "timestamp" to currentTime,
            "ppg" to ppg,
            "timeOffset" to timeOffset
        )

        val ppgIr = PpgIr(currentTime, ppg)
        val dataMap = ppgIr.toDataMap()

        assertEquals(expectedDataMap, dataMap)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `test toDataMap method for PpgRed`() {
        val timestamp = 12341234L
        val ppg = 13579
        val timeOffset = getCurrentTimeOffset()
        val expectedDataMap = mapOf(
            "timestamp" to timestamp,
            "ppg" to ppg,
            "timeOffset" to timeOffset
        )

        val ppgRed = PpgRed(timestamp, ppg)
        val dataMap = ppgRed.toDataMap()

        assertEquals(expectedDataMap, dataMap)
    }
}
