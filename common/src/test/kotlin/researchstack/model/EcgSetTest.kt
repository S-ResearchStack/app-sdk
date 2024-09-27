package researchstack.model

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.Ecg
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.PpgGreen
import researchstack.util.getCurrentTimeOffset

class EcgSetTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `EcgSet toDataMap method should work fine`() {
        val timestamp = 12341234L
        val ecgs = listOf(Ecg(timestamp, 1.0f))
        val ppgGreens = listOf(PpgGreen(timestamp, 2), PpgGreen(timestamp, 3))
        val leadOff = 1
        val maxThreshold = 3.0f
        val minThreshold = 2.0f
        val sequence = 1
        val sessionId = 123456789L
        val timeOffset = getCurrentTimeOffset()
        val expectedDataMap = mapOf(
            "ECG1_mv" to 1.0f,
            "LeadOff" to leadOff,
            "SessionId" to sessionId,
            "PktSeq#" to sequence,
            "PosThreshold_mv" to maxThreshold,
            "NegThreshold_mv" to minThreshold,
            "PPG_Green" to 2,
            "PPG_Green_Current" to 3,
            "timestamp" to timestamp,
            "time_offset" to timeOffset
        )

        val ecgSet = EcgSet(ecgs, ppgGreens, leadOff, maxThreshold, minThreshold, sequence)
        ecgSet.sessionId = sessionId
        val dataMap = ecgSet.toDataMap()

        assertEquals(expectedDataMap, dataMap)
    }
}
