package researchstack.model

import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.SpO2

class Spo2Test {
    @Tag(POSITIVE_TEST)
    @Test
    fun `Spo2 toDataMap should work fine`() {
        val timestamp = 12341234L
        val heartRate = 100
        val spO2 = 99
        val status: SpO2.Flag = SpO2.Flag.FAILED
        val spO2Value = SpO2(timestamp, heartRate, spO2, status)
        val dataMap = spO2Value.toDataMap()

        Assert.assertEquals(5, dataMap.size)
        Assert.assertEquals(timestamp, dataMap["timestamp"])
        Assert.assertEquals(heartRate, dataMap["heartRate"])
        Assert.assertEquals(spO2, dataMap["spO2"])
        Assert.assertEquals(5, dataMap["status"])
    }
}
