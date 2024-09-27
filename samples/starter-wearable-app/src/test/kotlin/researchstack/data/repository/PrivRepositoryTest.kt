package researchstack.data.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.HealthTracker.TrackerEventListener
import com.samsung.android.service.health.tracking.HealthTrackerException
import com.samsung.android.service.health.tracking.HealthTrackingService
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Tag
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.Timestamp

internal abstract class PrivRepositoryTest<T : Timestamp> {
    protected val trackerEventListener = slot<TrackerEventListener>()

    protected abstract val healthTrackerType: HealthTrackerType

    protected open var healthDataTracker = mockk<HealthTracker> {
    }
    protected open var healthTrackingService = mockk<HealthTrackingService> {
        every { getHealthTracker(any()) } returns healthDataTracker
    }

    abstract fun getTracker(): PrivRepository<T>

    @Before
    fun beforeEach() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        val isReady = mutableStateOf(PrivDataRequester.isConnected)
        PrivDataRequester.initialize(
            mockk<Activity>(),
            mockk<Context>(),
            onConnectionSuccess = { isReady.value = true }
        )
        PrivDataRequester.healthTrackingService = healthTrackingService
        justRun { healthDataTracker.unsetEventListener() }
        every { healthDataTracker.setEventListener(capture(trackerEventListener)) } answers {}
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun test() {
        PrivDataRequester.connectionListener.onConnectionSuccess()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun test_() {
        PrivDataRequester.connectionListener.onConnectionEnded()
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun test__() {
        PrivDataRequester.connectionListener.onConnectionFailed(HealthTrackerException("abc"))
    }

    protected suspend fun generateSensorEvent(count: Int) {
        var sendEventCount = 0
        do {
            while (!trackerEventListener.isCaptured) {
                delay(10)
            }
            delay(1000)
            trackerEventListener.captured.onDataReceived(createTrackerEvent())
            sendEventCount += 1
        } while (sendEventCount < count)
        delay(1000)
        getTracker().stopTracking()
    }

    protected suspend fun generateSensorErrorEvent(count: Int) {
        while (!trackerEventListener.isCaptured) {
            delay(10)
        }
        delay(1000)

        trackerEventListener.captured.onError(HealthTracker.TrackerError.PERMISSION_ERROR)
        trackerEventListener.captured.onDataReceived(createTrackerEvent())

        delay(1000)
        getTracker().stopTracking()
    }

    protected suspend fun generateSensorFlushCompletedEvent(count: Int) {
        var sendEventCount = 0
        do {
            while (!trackerEventListener.isCaptured) {
                delay(10)
            }
            delay(1000)
            trackerEventListener.captured.onFlushCompleted()
            sendEventCount += 1
        } while (sendEventCount < count)
        delay(1000)
        getTracker().stopTracking()
    }

    @Test
    fun `catch event when stop tracking failure`() = runTest {
        getTracker().startTracking()
        every { healthDataTracker.unsetEventListener() } throws Exception()
        getTracker().stopTracking()
    }

    abstract fun createTrackerEvent(): List<DataPoint>
}
