package researchstack.model

import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.FailStatusBIA
import researchstack.util.getCurrentTimeOffset

class BiaTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `Bia toDataMap method should work fine`() {
        val timestamp = 12341234L
        val basalMetabolicRate = 1000f
        val bodyFatMass = 10f
        val bodyFatRatio = 20f
        val fatFreeMass = 30f
        val fatFreeRatio = 40f
        val skeletalMuscleMass = 50f
        val skeletalMuscleRatio = 60f
        val totalBodyWater = 70f
        val measurementProgress = 80f
        val status = 0
        val timeOffset = getCurrentTimeOffset()
        val expectedMap = mapOf(
            "timestamp" to timestamp,
            "basalMetabolicRate" to basalMetabolicRate,
            "bodyFatMass" to bodyFatMass,
            "bodyFatRatio" to bodyFatRatio,
            "fatFreeMass" to fatFreeMass,
            "fatFreeRatio" to fatFreeRatio,
            "skeletalMuscleMass" to skeletalMuscleMass,
            "skeletalMuscleRatio" to skeletalMuscleRatio,
            "totalBodyWater" to totalBodyWater,
            "measurementProgress" to measurementProgress,
            "status" to status,
            "timeOffset" to timeOffset,
        )

        val bia = Bia(
            timestamp,
            basalMetabolicRate,
            bodyFatMass,
            bodyFatRatio,
            fatFreeMass,
            fatFreeRatio,
            skeletalMuscleMass,
            skeletalMuscleRatio,
            totalBodyWater,
            measurementProgress,
            status,
            timeOffset
        )
        val dataMap = bia.toDataMap()
        assertEquals(expectedMap, dataMap)
    }

    @Tag(POSITIVE_TEST)
    @Test
    fun `getStatus method should work fine`() {
        val sensorErrorFailStatusBIA = FailStatusBIA.SENSOR_ERROR
        val wristDetachedFailStatusBIA = FailStatusBIA.WRIST_DETACHED
        val fingerOnHomeButtonBrokenFailStatusBIA = FailStatusBIA.FINGER_ON_HOME_BUTTON_BROKEN
        val fingerOnBackButtonBrokenFailStatusBIA = FailStatusBIA.FINGER_ON_BACK_BUTTON_BROKEN
        val allFingerBrokenFailStatusBIA = FailStatusBIA.ALL_FINGER_BROKEN
        val wristLooseFailStatusBIA = FailStatusBIA.WRIST_LOOSE
        val dryFingerFailStatusBIA = FailStatusBIA.DRY_FINGER
        val bodyTooBigFailStatusBIA = FailStatusBIA.BODY_TOO_BIG
        val twoHandTouchedEachOtherFailStatusBIA = FailStatusBIA.TWO_HAND_TOUCHED_EACH_OTHER
        val allFingerContactSusFrameFailStatusBIA = FailStatusBIA.ALL_FINGER_CONTACT_SUS_FRAME
        val unstableImpedanceFailStatusBIA = FailStatusBIA.UNSTABLE_IMPEDANCE
        val bodyTooFatFailStatusBIA = FailStatusBIA.BODY_TOO_FAT

        val sensorErrorStatus = sensorErrorFailStatusBIA.status
        val wristDetachedStatus = wristDetachedFailStatusBIA.status
        val fingerOnHomeButtonBrokenStatus = fingerOnHomeButtonBrokenFailStatusBIA.status
        val fingerOnBackButtonBrokenStatus = fingerOnBackButtonBrokenFailStatusBIA.status
        val allFingerBrokenStatus = allFingerBrokenFailStatusBIA.status
        val wristLooseStatus = wristLooseFailStatusBIA.status
        val dryFingerStatus = dryFingerFailStatusBIA.status
        val bodyTooBigStatus = bodyTooBigFailStatusBIA.status
        val twoHandTouchedEachOtherStatus = twoHandTouchedEachOtherFailStatusBIA.status
        val allFingerContactSusFrameStatus = allFingerContactSusFrameFailStatusBIA.status
        val unstableImpedanceStatus = unstableImpedanceFailStatusBIA.status
        val bodyTooFatStatus = bodyTooFatFailStatusBIA.status

        assertEquals(2, sensorErrorStatus)
        assertEquals(4, wristDetachedStatus)
        assertEquals(7, fingerOnHomeButtonBrokenStatus)
        assertEquals(8, fingerOnBackButtonBrokenStatus)
        assertEquals(9, allFingerBrokenStatus)
        assertEquals(10, wristLooseStatus)
        assertEquals(11, dryFingerStatus)
        assertEquals(13, bodyTooBigStatus)
        assertEquals(14, twoHandTouchedEachOtherStatus)
        assertEquals(15, allFingerContactSusFrameStatus)
        assertEquals(17, unstableImpedanceStatus)
        assertEquals(18, bodyTooFatStatus)
    }
}
