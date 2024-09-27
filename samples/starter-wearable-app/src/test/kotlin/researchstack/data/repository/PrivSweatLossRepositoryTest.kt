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
import researchstack.data.local.room.dao.SweatLossDao
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.SweatLoss
import java.io.File
import java.io.FileNotFoundException

internal class PrivSweatLossRepositoryTest : PrivRepositoryTest<SweatLoss>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val sweatLossDao = mockk<SweatLossDao>()
    private val sweatLossFileRepository = FileRepository(SweatLoss::class, context, 999L)
    private val privSweatLossRepository by lazy {
        PrivSweatLossRepository(
            sweatLossFileRepository
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.SWEAT_LOSS

    override fun getTracker(): PrivRepository<SweatLoss> = privSweatLossRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val keySweatLoss = ValueKey.SweatLossSet.SWEAT_LOSS
        val valueSweatLoss = Value(10)
        val keyStatus = ValueKey.SweatLossSet.STATUS
        val valueStatus = Value(10)
        val dataPoint = DataPoint(
            mapOf(
                keySweatLoss to valueSweatLoss,
                keyStatus to valueStatus,
            )
        )
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
        privSweatLossRepository.startTracking()
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
            privSweatLossRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privSweatLossRepository.startTracking()
                    privSweatLossRepository.stopTracking()
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
        privSweatLossRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privSweatLossRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privSweatLossRepository.deleteFile()
        coEvery { privSweatLossRepository.deleteFile() } throws FileNotFoundException()
        val result = privSweatLossRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert data to file csv`() {
        privSweatLossRepository.insert(SweatLoss())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list data in csv file`() {
        privSweatLossRepository.insertAll(listOf(SweatLoss()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list data throw ClassCastException`() {
        val sweatLoss = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privSweatLossRepository.insertAll(listOf(sweatLoss as SweatLoss))
        }
    }
}
