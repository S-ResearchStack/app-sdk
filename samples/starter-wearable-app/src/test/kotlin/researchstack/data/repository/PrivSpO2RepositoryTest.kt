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
import researchstack.data.local.room.dao.SpO2Dao
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.SpO2
import java.io.File
import java.io.FileNotFoundException

internal class PrivSpO2RepositoryTest : PrivRepositoryTest<SpO2>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val spO2Dao = mockk<SpO2Dao>()
    private val spO2FileRepository = FileRepository(SpO2::class, context, 999L)
    private val privSpO2Repository by lazy {
        PrivSpO2Repository(
            spO2FileRepository
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.SPO2

    override fun getTracker(): PrivRepository<SpO2> = privSpO2Repository

    override fun createTrackerEvent(): List<DataPoint> {
        val keySpO2 = ValueKey.SpO2Set.SPO2
        val valueSpO2 = Value(10)
        val keyHeartRate = ValueKey.SpO2Set.HEART_RATE
        val valueHeartRate = Value(10)
        val keySpO2Status = ValueKey.SpO2Set.STATUS
        val valueSpO2Status = Value(10)
        val keySpO2AccuracyFlag = ValueKey.SpO2Set.ACCURACY_FLAG
        val valueSpO2AccuracyFlag = Value(10)
        val dataPoint = DataPoint(
            mapOf(
                keySpO2 to valueSpO2,
                keyHeartRate to valueHeartRate,
                keySpO2Status to valueSpO2Status,
                keySpO2AccuracyFlag to valueSpO2AccuracyFlag
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
        privSpO2Repository.startTracking()
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
            privSpO2Repository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privSpO2Repository.startTracking()
                    privSpO2Repository.stopTracking()
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
        privSpO2Repository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privSpO2Repository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privSpO2Repository.deleteFile()
        coEvery { privSpO2Repository.deleteFile() } throws FileNotFoundException()
        val result = privSpO2Repository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert data to file csv`() {
        privSpO2Repository.insert(SpO2())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list data in csv file`() {
        privSpO2Repository.insertAll(listOf(SpO2()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list data throw ClassCastException`() {
        val spO2 = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privSpO2Repository.insertAll(listOf(spO2 as SpO2))
        }
    }
}
