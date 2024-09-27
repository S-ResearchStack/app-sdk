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
import researchstack.data.local.room.dao.EcgDao
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.PpgRed
import java.io.File
import java.io.FileNotFoundException

internal class PrivEcgRepositoryTest : PrivRepositoryTest<EcgSet>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val ecgDao = mockk<EcgDao>()
    private val ecgFileRepository = FileRepository(EcgSet::class, context, 999L)
    private val privEcgRepository by lazy {
        PrivEcgRepository(
            ecgFileRepository,
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.ECG

    override fun getTracker(): PrivRepository<EcgSet> = privEcgRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val keyEcg = ValueKey.EcgSet.ECG_MV
        val valueEcg = Value(10)
        val keyLeadOff = ValueKey.EcgSet.LEAD_OFF
        val valueLeadOff = Value(10)
        val keyPpgGreen = ValueKey.EcgSet.PPG_GREEN
        val valuePpgGreen = Value(10)
        val keyMax = ValueKey.EcgSet.MAX_THRESHOLD_MV
        val valueMax = Value(10)
        val keyMin = ValueKey.EcgSet.MIN_THRESHOLD_MV
        val valueMin = Value(10)
        val keySequence = ValueKey.EcgSet.SEQUENCE
        val valueSequence = Value(10)
        val dataPoint = DataPoint(
            mapOf(
                keyEcg to valueEcg,
                keyPpgGreen to valuePpgGreen,
                keyLeadOff to valueLeadOff,
                keyMax to valueMax,
                keyMin to valueMin,
                keySequence to valueSequence
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
        privEcgRepository.startTracking()
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
            privEcgRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privEcgRepository.startTracking()
                    privEcgRepository.stopTracking()
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
        privEcgRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `delete file csv`() {
        privEcgRepository.deleteFile()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privEcgRepository.deleteFile()
        coEvery { privEcgRepository.deleteFile() } throws FileNotFoundException()
        val result = privEcgRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert data to file csv`() {
        val ecgSet = EcgSet()
        privEcgRepository.insert(ecgSet)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list data in csv file`() {
        privEcgRepository.insertAll(listOf(EcgSet()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list data throw ClassCastException`() {
        val ecgSet = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privEcgRepository.insertAll(listOf(ecgSet as EcgSet))
        }
    }
}
