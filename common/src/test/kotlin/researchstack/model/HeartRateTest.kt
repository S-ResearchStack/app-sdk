package researchstack.model

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.HeartRate
import researchstack.util.getCurrentTimeOffset

class HeartRateTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `HeartRate toDataMap method should work fine`() {
        val timestamp = 12341234L
        val value = 70
        val ibiList = listOf(100, 200, 300)
        val ibiStatusList = listOf(1, 2, 3)
        val heartRateStatus = 4
        val timeOffset = getCurrentTimeOffset()
        val expectedDataMap = mapOf(
            "timestamp" to timestamp,
            "ibiList" to ibiList,
            "ibiStatusList" to ibiStatusList,
            "heartRateStatus" to heartRateStatus,
            "value" to value,
            "timeOffset" to timeOffset
        )

        val heartRate = HeartRate(timestamp, value, ibiList, ibiStatusList, heartRateStatus)
        val dataMap = heartRate.toDataMap()

        assertEquals(expectedDataMap, dataMap)
    }
}
