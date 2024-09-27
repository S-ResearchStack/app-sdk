package researchstack.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.SweatLoss

class SweatLossTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `SweatLoss toDataMap should work fine`() {
        val timestamp = 12341234L
        val sweatLoss = 500f
        val status = 1
        val sweatLossInstance = SweatLoss(timestamp, sweatLoss, status)
        val dataMap = sweatLossInstance.toDataMap()

        assertEquals(4, dataMap.size)
        assertEquals(timestamp, dataMap["timestamp"])
        assertEquals(sweatLoss, dataMap["sweatLoss"])
        assertEquals(status, dataMap["status"])
    }
}
