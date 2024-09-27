package researchstack.data.repository.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.paging.PagingSource
import aws.smithy.kotlin.runtime.time.Instant
import aws.smithy.kotlin.runtime.time.epochMilliseconds
import com.google.android.gms.location.LocationResult
import io.grpc.Status
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.channels.ChannelResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.data.datasource.local.room.dao.SpeedDao
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.data.datasource.sensor.LocationTracker

class SpeedRepositoryImplTest {

    private val healthDataOutPort = mockk<HealthDataOutPort>()
    private val speedDao = mockk<SpeedDao> {
        every { insert(any()) } returns Unit
        every { insertAll(any()) } returns Unit
        every { getGreaterThan(any()) } returns mockk<PagingSource<Int, Speed>>()
        every { deleteLEThan(0L) } answers {}
    }

    private val context = mockk<Context>()
    private val packageManager = mockk<PackageManager>()
    private val myLocationCallback = slot<LocationTracker.MyLocationCallback>()

    private val speedRepository by lazy {
        spyk(
            SpeedRepositoryImpl(
                context, speedDao, GrpcHealthDataSynchronizerImpl(healthDataOutPort), mockk()
            )
        )
    }

    private suspend fun generateLocationEvent(count: Int) {
        var sendEventCount = 0
        do {
            while (!myLocationCallback.isCaptured) {
                delay(10)
            }
            delay(1000)
            myLocationCallback.captured.let {
                val locationResult = LocationResult.create(listOf(Location("")))
                it.onLocationResult(locationResult)
            }
            sendEventCount += 1
        } while (sendEventCount < count)
        delay(1000)
        speedRepository.stopTracking()
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `startTracking should collect events`() {
        every { context.applicationContext } returns context
        every { context.packageManager } returns packageManager
        every { context.checkPermission(any(), 0, 0) } returns PackageManager.PERMISSION_GRANTED
        val flow = speedRepository.startTracking()
        Assertions.assertNotNull(flow)
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `startTracking should throw IllegalStateException`() = runTest {
        every { context.applicationContext } returns context
        every { context.packageManager } returns packageManager
        every { context.checkPermission(any(), 0, 0) } returns PackageManager.PERMISSION_GRANTED

        this.backgroundScope.launch {
            runCatching {
                generateLocationEvent(5)
            }.onFailure {
                Assertions.assertTrue(it is IllegalStateException)
            }
        }

        runCatching {
            speedRepository.startTracking().collect {
                println("startTracking collect $it")
            }
        }.onFailure {
            Assertions.assertTrue(it is IllegalStateException)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `MyLocationCallback#getmSpeed should return failure`() {
        val myLocationCallback = LocationTracker.MyLocationCallback()
        val locationResult = LocationResult.create(listOf(Location("")))
        myLocationCallback.onLocationResult(locationResult)

        Assertions.assertTrue(runCatching { myLocationCallback.mSpeed }.isFailure)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `MyLocationCallback#getmSpeed should return not null`() {
        val locationResult = LocationResult.create(listOf(Location("")))
        val myLocationCallback = LocationTracker.MyLocationCallback().apply {
            mLastLocation = locationResult.lastLocation
            mProducerScope = mockk<ProducerScope<Speed>> {
                every { trySend(any()) } returns mockk<ChannelResult<Unit>>()
            }
        }

        myLocationCallback.onLocationResult(locationResult)

        Assertions.assertNotNull(myLocationCallback.mSpeed)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `MyLocationCallback#getmSpeed should return not null when mLastLocation#hasSpeed()`() {
        val locationResult = spyk(LocationResult.create(listOf(Location("")))) {
            every { lastLocation } returns mockk<Location> {
                every { hasSpeed() } returns true
                every { speed } returns 0f
                every { time } returns System.currentTimeMillis()
            }
        }

        val myLocationCallback = LocationTracker.MyLocationCallback().apply {
            mLastLocation = locationResult.lastLocation
            mProducerScope = mockk<ProducerScope<Speed>> {
                every { trySend(any()) } returns mockk<ChannelResult<Unit>>()
            }
        }

        myLocationCallback.onLocationResult(locationResult)

        Assertions.assertNotNull(myLocationCallback.mSpeed)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `insert insertAll getGreaterThan deleteLEThan should not throw exception`() {
        Assertions.assertDoesNotThrow {
            val speed = Speed(
                0f,
                System.currentTimeMillis() - 1000,
                System.currentTimeMillis()
            )
            speedRepository.insert(speed)
            speedRepository.insertAll(speed)
            speedRepository.getGreaterThan(0)

            speedRepository.deleteLEThan(0L)
            speedRepository.getGreaterThan(0)
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `stopTracking should not throw exception if tracking was not started`() = runTest {
        assertDoesNotThrow {
            speedRepository.stopTracking()
        }
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure when studyId is empty`() = runTest {
        Assertions.assertTrue(
            speedRepository.sync(emptyList()).isFailure
        )
    }

    // NOTE: We should update proto
    // @Test
    @Tag(POSITIVE_TEST)
    fun `sync should return success`() = runTest {
        var nextkey = 2
        val loadResult = mockk<PagingSource.LoadResult.Page<Int, Speed>> {
            every { data } returns listOf(
                Speed(10f, Instant.now().epochMilliseconds, Instant.now().epochMilliseconds + 1000L)
            )
            every { nextKey } answers {
                nextkey -= 1
                if (nextkey == 0) null else nextkey
            }
        }

        every { speedDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { speedDao.deleteLEThan(any<Long>()) }

        coEvery { healthDataOutPort.syncHealthData(any(), any()) } returns Result.success(Unit)

        Assertions.assertTrue(
            speedRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }.isSuccess
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if fail to upload data`() = runTest {
        val loadResult = mockk<PagingSource.LoadResult.Page<Int, Speed>> {
            every { data } returns listOf(
                Speed(20f, Instant.now().epochMilliseconds, Instant.now().epochMilliseconds)
            )
        }

        every { speedDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { speedDao.deleteLEThan(any<Long>()) }

        coEvery {
            healthDataOutPort.syncHealthData(
                any(), any()
            )
        } throws Status.INTERNAL.asException()

        Assertions.assertTrue(
            speedRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }.isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if load result is invalid`() = runTest {
        val loadResult = mockk<PagingSource.LoadResult.Invalid<Int, Speed>>()

        every { speedDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { speedDao.deleteLEThan(any<Long>()) }

        Assertions.assertTrue(
            speedRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }.isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `sync should return failure if load result is error`() = runTest {
        val loadResult = mockk<PagingSource.LoadResult.Error<Int, Speed>> {
            every { throwable } returns RuntimeException("")
        }

        every { speedDao.getGreaterThan(any()) } returns mockk {
            coEvery { load(any()) } returns loadResult
        }

        justRun { speedDao.deleteLEThan(any<Long>()) }

        Assertions.assertTrue(
            speedRepository.sync(listOf("studyId"))
                .onFailure { println(it.stackTraceToString()) }.isFailure
        )
    }
}
