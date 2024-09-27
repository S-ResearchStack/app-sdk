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
import researchstack.data.local.room.dao.AccelerometerDao
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.PpgRed
import java.io.File
import java.io.FileNotFoundException

internal class PrivAccMeterRepositoryTest : PrivRepositoryTest<Accelerometer>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val accelerometerDao = mockk<AccelerometerDao>()
    private val accMeterFileRepository = FileRepository(Accelerometer::class, context, 999L)
    private val privAccMeterRepository by lazy {
        PrivAccMeterRepository(
            accMeterFileRepository
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.ACCELEROMETER

    override fun getTracker(): PrivRepository<Accelerometer> = privAccMeterRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val keyX = ValueKey.AccelerometerSet.ACCELEROMETER_X
        val valueX = Value(10)
        val keyY = ValueKey.AccelerometerSet.ACCELEROMETER_Y
        val valueY = Value(10)
        val keyZ = ValueKey.AccelerometerSet.ACCELEROMETER_Z
        val valueZ = Value(10)
        val dataPoint = DataPoint(mapOf(keyX to valueX, keyY to valueY, keyZ to valueZ))
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
        privAccMeterRepository.startTracking()
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
            privAccMeterRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privAccMeterRepository.startTracking()
                    privAccMeterRepository.stopTracking()
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
        privAccMeterRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privAccMeterRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privAccMeterRepository.deleteFile()
        coEvery { privAccMeterRepository.deleteFile() } throws FileNotFoundException()
        val result = privAccMeterRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert ppgIr data to file csv`() {
        val accelerometer = Accelerometer()
        privAccMeterRepository.insert(accelerometer)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list ppgIr data in csv file`() {
        privAccMeterRepository.insertAll(listOf(Accelerometer()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list ppgIr data throw ClassCastException`() {
        val accelerometer = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privAccMeterRepository.insertAll(listOf(accelerometer as Accelerometer))
        }
    }
}
