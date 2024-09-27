package researchstack.data.repository

import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.pref.DataStoreTestHelper
import researchstack.data.local.pref.TrackMeasureTimePref
import java.io.File

internal class TrackMeasureTimeRepositoryImplTest :
    DataStoreTestHelper("measure-time.preferences_pb") {
    private val trackMeasureTimePref = TrackMeasureTimePref(testDataStore)
    private val trackMeasureTimeRepositoryImpl =
        TrackMeasureTimeRepositoryImpl(trackMeasureTimePref)
    private val prefKey = longPreferencesKey("measure-key")

    @Before
    fun beforeEach() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `get measure time value should zero`() = runTest {
        File(tempDir, fileName).apply {
            deleteRecursively()
        }
        this.backgroundScope.launch {
            trackMeasureTimeRepositoryImpl.getLastMeasureTime(prefKey).collect {
                Assertions.assertNull(it)
            }
        }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `save and get measure time value should be success`() = runTest {
        trackMeasureTimeRepositoryImpl.add(prefKey, 10)
        val result = trackMeasureTimeRepositoryImpl.getLastMeasureTime(prefKey)
        Assertions.assertNotNull(result)
    }
}
