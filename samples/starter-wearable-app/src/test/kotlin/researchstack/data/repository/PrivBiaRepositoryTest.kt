package researchstack.data.repository

import android.content.Context
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.Value
import com.samsung.android.service.health.tracking.data.ValueKey
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.local.file.FileRepository
import researchstack.data.local.room.dao.BiaDao
import researchstack.domain.model.Gender
import researchstack.domain.model.UserProfile
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.PpgRed
import researchstack.requiredinterface.UserProfileAccessible
import java.io.File
import java.io.FileNotFoundException

internal class PrivBiaRepositoryTest : PrivRepositoryTest<Bia>() {
    private val context = mockk<Context> {
        every { filesDir } returns File("build/shr_test")
    }
    private val biaDao = mockk<BiaDao>()
    private val userProfile = UserProfile(0f, 0f, 0, Gender.FEMALE, true)
    private val userProfileRepository = mockk<UserProfileAccessible> {
        every { getUserProfile() } returns flowOf(userProfile)
    }
    private val biaFileRepository = FileRepository(Bia::class, context, 999L)
    private val privBiaRepository by lazy {
        PrivBiaRepository(
            userProfileRepository,
            biaFileRepository,
        )
    }
    override val healthTrackerType: HealthTrackerType = HealthTrackerType.ACCELEROMETER

    override fun getTracker(): PrivRepository<Bia> = privBiaRepository

    override fun createTrackerEvent(): List<DataPoint> {
        val arr = listOf(
            ValueKey.BiaSet.BASAL_METABOLIC_RATE,
            ValueKey.BiaSet.BODY_FAT_MASS,
            ValueKey.BiaSet.FAT_FREE_MASS,
            ValueKey.BiaSet.PROGRESS,
            ValueKey.BiaSet.SKELETAL_MUSCLE_MASS,
            ValueKey.BiaSet.FAT_FREE_RATIO,
            ValueKey.BiaSet.BODY_FAT_RATIO,
            ValueKey.BiaSet.TOTAL_BODY_WATER,
            ValueKey.BiaSet.STATUS,
        )

        val map = mutableMapOf<ValueKey<*>, Value<*>>()
        arr.forEach {
            map[it] = Value(10f)
        }
        val dataPoint = DataPoint(map)
        return listOf(dataPoint)
    }

    @Before
    fun setUp() {
        every {
            healthTrackingService.getHealthTracker(
                any(), any()
            )
        } returns healthDataTracker
    }

    @After
    fun tearDown() {
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `startTracking should run error event`() = runTest {
        launch {
            generateSensorErrorEvent(5)
        }

        var receiveEventCount = 0
        runCatching {
            privBiaRepository.startTracking()
                .collect {
                    receiveEventCount += 1
                    privBiaRepository.startTracking()
                    privBiaRepository.stopTracking()
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
        privBiaRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert list data in csv file`() {
        privBiaRepository.insertAll(listOf(Bia()))
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `insert list data throw ClassCastException`() {
        val bia = PpgRed()
        Assert.assertThrows(ClassCastException::class.java) {
            privBiaRepository.insertAll(listOf(bia as Bia))
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `delete file csv throw exception`() {
        privBiaRepository.deleteFile()
        coEvery { privBiaRepository.deleteFile() } throws FileNotFoundException()
        val result = privBiaRepository.deleteFile()
        Assertions.assertTrue(result.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert data to file csv`() {
        privBiaRepository.insert(Bia())
    }
}
