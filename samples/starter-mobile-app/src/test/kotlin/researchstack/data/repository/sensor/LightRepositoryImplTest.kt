package researchstack.data.repository.sensor

import android.hardware.SensorEvent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource.LoadResult.Invalid
import androidx.paging.PagingSource.LoadResult.Page
import aws.smithy.kotlin.runtime.time.Instant
import aws.smithy.kotlin.runtime.time.epochMilliseconds
import io.grpc.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.io.TempDir
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.data.datasource.local.room.dao.LightDao
import researchstack.data.datasource.sensor.SensorTracker
import researchstack.domain.model.sensor.Light
import java.io.File
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class LightRepositoryImplTest : SensorTrackerTestHelper<Light>() {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    @TempDir
    private var tempDir: File? = null

    private val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope.backgroundScope,
        produceFile = { File(tempDir, "app.preferences_pb") }
    )

    private val lightDao = mockk<LightDao>()

    private val lightRepository by lazy {
        LightRepositoryImpl(
            sensorManager,
            lightDao,
            GrpcHealthDataSynchronizerImpl(healthDataOutPort),
            testDataStore
        )
    }

    override fun getTracker(): SensorTracker<Light> = lightRepository

    @Test
    @Tag(POSITIVE_TEST)
    fun `startTracking should collect sensor-events`() = runTest {
        val sendEventCount = 5

        this.backgroundScope.launch {
            generateSensorEvent(5)
        }

        var receiveEventCount = 0
        lightRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }

        assertEquals(sendEventCount, receiveEventCount)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `stopTracking should not throw exception if tracking was not started`() = runTest {
        assertDoesNotThrow {
            lightRepository.stopTracking()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure when studyId is empty`() = runTest {
        assertTrue(
            lightRepository.sync(emptyList())
                .isFailure
        )
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `sync should return success`() = testScope.runTest {
        var nextkey = 2
        val loadResult = mockk<Page<Int, Light>> {
            every { data } returns listOf(
                Light(1, 1, Instant.now().epochMilliseconds)
            )
            every { nextKey } answers {
                nextkey -= 1
                if (nextkey == 0) null else nextkey
            }
        }

        every { lightDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { lightDao.deleteLEThan(any<Long>()) }

        coEvery { healthDataOutPort.syncHealthData(any(), any()) } returns Result.success(Unit)

        assertTrue(
            lightRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }
                .isSuccess
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if fail to upload data`() = testScope.runTest {
        val loadResult = mockk<Page<Int, Light>> {
            every { data } returns listOf(
                Light(1, 1, Instant.now().epochMilliseconds)
            )
        }

        every { lightDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { lightDao.deleteLEThan(any<Long>()) }

        coEvery { healthDataOutPort.syncHealthData(any(), any()) } throws Status.INTERNAL.asException()

        assertTrue(
            lightRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }
                .isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if load result is invalid`() = testScope.runTest {
        val loadResult = mockk<Invalid<Int, Light>>()

        every { lightDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { lightDao.deleteLEThan(any<Long>()) }

        assertTrue(
            lightRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }
                .isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if load result is error`() = testScope.runTest {
        val loadResult = mockk<LoadResult.Error<Int, Light>> {
            every { throwable } returns RuntimeException("")
        }

        every { lightDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { lightDao.deleteLEThan(any<Long>()) }

        assertTrue(
            lightRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }
                .isFailure
        )
    }

    override fun createSensorEvent(): SensorEvent {
        val sensorEvent = spyk<SensorEvent>()

        val f = SensorEvent::class.java.getDeclaredField("values")
        f.isAccessible = true
        f.set(sensorEvent, floatArrayOf(Random.nextFloat()))
        return sensorEvent
    }
}
