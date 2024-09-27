package researchstack.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss

class PrivDataTypeTest {

    @Tag(POSITIVE_TEST)
    @Test
    fun `test fromModel method with valid model`() {
        val models = listOf(
            Accelerometer::class,
            Bia::class,
            EcgSet::class,
            PpgGreen::class,
            PpgIr::class,
            PpgRed::class,
            SpO2::class,
            SweatLoss::class,
            HeartRate::class,
        )
        val expectedPrivDataTypes = listOf(
            PrivDataType.WEAR_ACCELEROMETER,
            PrivDataType.WEAR_BIA,
            PrivDataType.WEAR_ECG,
            PrivDataType.WEAR_PPG_GREEN,
            PrivDataType.WEAR_PPG_IR,
            PrivDataType.WEAR_PPG_RED,
            PrivDataType.WEAR_SPO2,
            PrivDataType.WEAR_SWEAT_LOSS,
            PrivDataType.WEAR_HEART_RATE,
        )
        val actualPrivDataTypes = models.map { PrivDataType.fromModel(it) }
        assertEquals(expectedPrivDataTypes, actualPrivDataTypes)
    }

    @Tag(NEGATIVE_TEST)
    @Test
    fun `test fromModel method with invalid model`() {
        val invalidModel = Any::class
        assertThrows(IllegalArgumentException::class.java) {
            PrivDataType.fromModel(invalidModel)
        }
    }
}
