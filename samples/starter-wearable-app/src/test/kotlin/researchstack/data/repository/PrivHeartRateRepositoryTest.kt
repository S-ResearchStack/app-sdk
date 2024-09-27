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
import researchstack.data.local.room.dao.HeartRateDao
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgRed
import java.io.File
import java.io.FileNotFoundException

internal class PrivHeartRateRepositoryTest : PrivRepositoryTest<HeartRate>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val heartRateDao = mockk<HeartRateDao>()
    private val heartRateFileRepository = FileRepository(HeartRate::class, context, 999L)
    private val privHeartRateRepository by lazy {
        PrivHeartRateRepository(
            heartRateFileRepository
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.HEART_RATE

    override fun getTracker(): PrivRepository<HeartRate> = privHeartRateRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val keyHeartRate = ValueKey.HeartRateSet.HEART_RATE
        val valueHeartRate = Value(10)
        val keyHeartRateStatus = ValueKey.HeartRateSet.HEART_RATE_STATUS
        val valueHeartRateStatus = Value(10)
        val keyHeartRateIbiList = ValueKey.HeartRateSet.IBI_LIST
        val valueHeartRateIbiList = Value(listOf(10))
        val keyHeartRateIbiStatusList = ValueKey.HeartRateSet.IBI_STATUS_LIST
        val valueHeartRateIbiStatusList = Value(listOf(1))
        val dataPoint = DataPoint(
            mapOf(
                keyHeartRate to valueHeartRate,
                keyHeartRateIbiList to valueHeartRateIbiList,
                keyHeartRateIbiStatusList to valueHeartRateIbiStatusList,
                keyHeartRateStatus to valueHeartRateStatus
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
        privHeartRateRepository.startTracking()
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
            privHeartRateRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privHeartRateRepository.startTracking()
                    privHeartRateRepository.stopTracking()
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
        privHeartRateRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privHeartRateRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privHeartRateRepository.deleteFile()
        coEvery { privHeartRateRepository.deleteFile() } throws FileNotFoundException()
        val result = privHeartRateRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert data to file csv`() {
        privHeartRateRepository.insert(HeartRate())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list data in csv file`() {
        privHeartRateRepository.insertAll(listOf(HeartRate()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list data throw ClassCastException`() {
        val heartRate = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privHeartRateRepository.insertAll(listOf(heartRate as HeartRate))
        }
    }
}
