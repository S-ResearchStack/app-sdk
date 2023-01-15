package healthstack.app.pref

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.time.Instant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingPreferenceTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testTaskSyncTime() {
        val settings = SettingPreference(context)

        runTest {
            val now = Instant.now()
            settings.setTaskSyncTime(now.toString())

            val taskSyncTime = settings.taskSyncTime.first()
            assertEquals(now.toString(), taskSyncTime)
        }
    }
}
