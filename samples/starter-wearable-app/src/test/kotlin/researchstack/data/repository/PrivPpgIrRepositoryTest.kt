package researchstack.data.repository

import android.content.Context
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.Value
import com.samsung.android.service.health.tracking.data.ValueKey
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.file.FileRepository
import researchstack.data.local.room.dao.PpgIrDao
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import java.io.File
import java.io.FileNotFoundException

internal class PrivPpgIrRepositoryTest : PrivRepositoryTest<PpgIr>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val ppgIrDao = mockk<PpgIrDao>()
    private val ppgIrFileRepository = FileRepository(PpgIr::class, context, 999L)
    private val privPpgIrRepository by lazy {
        PrivPpgIrRepository(
            ppgIrFileRepository,
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_IR

    override fun getTracker(): PrivRepository<PpgIr> = privPpgIrRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val key = ValueKey.PpgIrSet.PPG_IR
        val value = Value(10)
        val dataPoint = DataPoint(mapOf(key to value))
        return listOf(dataPoint)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `startTracking should collect sensor-events`() = runTest {
        val sendEventCount = 5

        launch {
            generateSensorEvent(5)
        }

        var receiveEventCount = 0
        privPpgIrRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }

        Assertions.assertEquals(sendEventCount, receiveEventCount)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `startTracking should run error event`() = runTest {
        launch {
            generateSensorErrorEvent(5)
        }

        var receiveEventCount = 0
        runCatching {
            privPpgIrRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privPpgIrRepository.startTracking()
                    privPpgIrRepository.stopTracking()
                }
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `startTracking should show FlushCompleted event`() = runTest {
        launch {
            generateSensorFlushCompletedEvent(5)
        }
        var receiveEventCount = 0
        privPpgIrRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() = runTest {
        privPpgIrRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() = runTest {
        privPpgIrRepository.deleteFile()
        coEvery { privPpgIrRepository.deleteFile() } throws FileNotFoundException()
        val result = privPpgIrRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert ppgIr data to file csv`() {
        val x = PpgIr(timestamp = System.currentTimeMillis(), ppg = 999, timeOffset = 9999)
        privPpgIrRepository.insert(x)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list ppgIr data in csv file`() {
        privPpgIrRepository.insertAll(listOf(PpgIr()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list ppgIr data throw ClassCastException`() {
        val ppgIr = PpgGreen()
        Assert.assertThrows(ClassCastException::class.java) {
            privPpgIrRepository.insertAll(listOf(ppgIr as PpgIr))
        }
    }
}
