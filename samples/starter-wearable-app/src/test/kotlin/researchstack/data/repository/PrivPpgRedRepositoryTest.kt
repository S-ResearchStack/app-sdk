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
import researchstack.data.local.room.dao.PpgRedDao
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgRed
import java.io.File
import java.io.FileNotFoundException

internal class PrivPpgRedRepositoryTest : PrivRepositoryTest<PpgRed>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val ppgRedDao = mockk<PpgRedDao>()
    private val ppgRedFileRepository = FileRepository(PpgRed::class, context, 999L)
    private val privPpgRedRepository by lazy {
        PrivPpgRedRepository(
            ppgRedFileRepository,
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.PPG_RED

    override fun getTracker(): PrivRepository<PpgRed> = privPpgRedRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val key = ValueKey.PpgRedSet.PPG_RED
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
        privPpgRedRepository.startTracking()
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
            privPpgRedRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privPpgRedRepository.startTracking()
                    privPpgRedRepository.stopTracking()
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
        privPpgRedRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privPpgRedRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privPpgRedRepository.deleteFile()
        coEvery { privPpgRedRepository.deleteFile() } throws FileNotFoundException()
        val result = privPpgRedRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert ppgIr data to file csv`() {
        val ppgRed = PpgRed(timestamp = System.currentTimeMillis(), ppg = 999, timeOffset = 9999)
        privPpgRedRepository.insert(ppgRed)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list ppgIr data in csv file`() {
        privPpgRedRepository.insertAll(listOf(PpgRed()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list ppgIr data throw ClassCastException`() {
        val ppgRed = PpgGreen()
        Assert.assertThrows(ClassCastException::class.java) {
            privPpgRedRepository.insertAll(listOf(ppgRed as PpgRed))
        }
    }
}
