package researchstack.data.repository

import androidx.datastore.core.IOException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.pref.DataStoreTestHelper
import researchstack.data.local.pref.WearableMeasurementPref

internal class SetEcgMeasurementRepositoryImplTest : DataStoreTestHelper("setEcg.preferences_pb") {
    private val wearableMeasurementPref = mockk<WearableMeasurementPref>()
    private val setEcgMeasurementRepositoryImpl =
        SetEcgMeasurementRepositoryImpl(wearableMeasurementPref)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `set ecg measurement status throw exception`() = runTest {
        this.backgroundScope.launch {
            coEvery { wearableMeasurementPref.setEcgMeasurementEnabled(false) } throws IOException()
            val result = kotlin.runCatching {
                setEcgMeasurementRepositoryImpl.setEcgMeasurementEnabled(false)
            }
            Assertions.assertFalse(result.isFailure)
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `set ecg measurement status should success`() = runTest {
        coEvery { wearableMeasurementPref.setEcgMeasurementEnabled(false) } returns Unit
        this.backgroundScope.launch {
            val result = kotlin.runCatching {
                setEcgMeasurementRepositoryImpl.setEcgMeasurementEnabled(false)
            }
            Assertions.assertTrue(result.isSuccess)
        }
    }
}
