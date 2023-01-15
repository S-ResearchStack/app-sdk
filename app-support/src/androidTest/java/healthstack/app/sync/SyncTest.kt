package healthstack.app.sync

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import healthstack.app.sync.SyncManager.HealthDataSyncSpec
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.healthdata.link.HealthData
import healthstack.healthdata.link.HealthDataLink
import healthstack.healthdata.link.HealthDataLinkHolder
import io.mockk.coEvery
import io.mockk.mockk
import java.util.concurrent.TimeUnit
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SyncTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @After
    fun tearDown() {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(SyncWorker::class.qualifiedName!!)
    }

    @Test
    @Throws(Exception::class)
    fun testRegisterSyncWorker() {
        val healthDataSyncSpec = HealthDataSyncSpec(
            "HeartRate",
            15L,
            TimeUnit.MINUTES,
        )
        val backendFacade = mockk<BackendFacade>()
        BackendFacadeHolder.initialize(backendFacade)

        val healthDataLink = mockk<HealthDataLink>()
        HealthDataLinkHolder.initialize(healthDataLink)
        coEvery { healthDataLink.getHealthData(any(), any(), any()) } returns
            HealthData(healthDataSyncSpec.healthDataTypeString, emptyList())

        SyncManager.initialize(context, listOf(healthDataSyncSpec))
        SyncManager.getInstance().startBackgroundSync()

        val workInfos = WorkManager.getInstance(context)
            .getWorkInfosByTag(SyncWorker::class.qualifiedName!!)
            .get()

        assertEquals(1, workInfos.size)
    }
}
