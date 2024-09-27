package researchstack.data.local.pref

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.POSITIVE_TEST
import researchstack.domain.model.priv.PrivDataType

internal class PrivDataOnOffPrefTest : DataStoreTestHelper("data.preferences_pb") {
    private val context = mockk<Context> {
        every { applicationContext } returns this
        every { filesDir } returns tempDir
    }
    private val prefKey = PrivDataOnOffPref.PERMITTED_DATA_PREF_KEY
    private val privDataOnOffPref = spyk(PrivDataOnOffPref(context.dataStore, prefKey))

    @Test
    @Tag(POSITIVE_TEST)
    fun `add priv data on or off to pref must be success`() = runTest {
        var isFailure = false
        PrivDataType.entries.forEach {
            runCatching { privDataOnOffPref.add(it) }.onFailure {
                println(it.stackTraceToString())
                isFailure = true
            }
            privDataOnOffPref.privDataTypesFlow.first()
        }
        Assertions.assertFalse(isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `remove priv data on or off to pref must be success`() = runTest {
        var isFailure = false
        PrivDataType.entries.forEach {
            runCatching { privDataOnOffPref.remove(it) }.onFailure {
                println(it.stackTraceToString())
                isFailure = true
            }
            privDataOnOffPref.privDataTypesFlow.first()
        }
        Assertions.assertFalse(isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `clear priv data on or off to pref must be success`() = runTest {
        var isFailure = false
        runCatching { privDataOnOffPref.clear() }.onFailure {
            println(it.stackTraceToString())
            isFailure = true
        }
        privDataOnOffPref.privDataTypesFlow.first()
        Assertions.assertFalse(isFailure)
    }
}
