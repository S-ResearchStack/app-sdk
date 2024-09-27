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
import researchstack.data.local.room.dao.PpgGreenDao
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgRed
import java.io.File
import java.io.FileNotFoundException

internal class PrivPpgGreenRepositoryTest : PrivRepositoryTest<PpgGreen>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val ppgGreenDao = mockk<PpgGreenDao>()
    private val ppgGreenFileRepository = FileRepository(PpgGreen::class, context, 999L)
    private val privPpgGreenRepository by lazy {
        PrivPpgGreenRepository(
            ppgGreenFileRepository,
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_GREEN

    override fun getTracker(): PrivRepository<PpgGreen> = privPpgGreenRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val key = ValueKey.PpgGreenSet.PPG_GREEN
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
        privPpgGreenRepository.startTracking()
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
            privPpgGreenRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privPpgGreenRepository.startTracking()
                    privPpgGreenRepository.stopTracking()
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
        privPpgGreenRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privPpgGreenRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privPpgGreenRepository.deleteFile()
        coEvery { privPpgGreenRepository.deleteFile() } throws FileNotFoundException()
        val result = privPpgGreenRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert ppgIr data to file csv`() {
        val ppgGreen = PpgGreen(timestamp = System.currentTimeMillis(), ppg = 999, timeOffset = 9999)
        privPpgGreenRepository.insert(ppgGreen)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list ppgIr data in csv file`() {
        privPpgGreenRepository.insertAll(listOf(PpgGreen()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list ppgIr data throw ClassCastException`() {
        val ppgGreen = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privPpgGreenRepository.insertAll(listOf(ppgGreen as PpgGreen))
        }
    }
}
