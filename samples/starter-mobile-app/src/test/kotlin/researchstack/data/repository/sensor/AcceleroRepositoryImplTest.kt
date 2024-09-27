package researchstack.data.repository.sensor

import android.hardware.SensorEvent
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.POSITIVE_TEST
import researchstack.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.data.datasource.local.room.dao.AccelerometerDao
import researchstack.data.datasource.sensor.SensorTracker
import researchstack.domain.model.sensor.Accelerometer
import kotlin.random.Random

internal class AcceleroRepositoryImplTest : SensorTrackerTestHelper<Accelerometer>() {
    private val acceleroRepository by lazy {
        AcceleroRepositoryImpl(
            sensorManager,
            mockk<AccelerometerDao>(),
            GrpcHealthDataSynchronizerImpl(healthDataOutPort),
            mockk(),
        )
    }

    override fun getTracker(): SensorTracker<Accelerometer> = acceleroRepository
    override fun createSensorEvent(): SensorEvent {
        val sensorEvent = spyk<SensorEvent>()

        val f = SensorEvent::class.java.getDeclaredField("values")
        f.isAccessible = true
        f.set(sensorEvent, floatArrayOf(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()))
        return sensorEvent
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `startTracking should collect sensor-events`() = runTest {
        val sendEventCount = 5

        this.backgroundScope.launch {
            generateSensorEvent(5)
        }

        var receiveEventCount = 0
        acceleroRepository.startTracking()
            .collect {
                receiveEventCount += 1
            }

        Assertions.assertEquals(sendEventCount, receiveEventCount)
    }
}
