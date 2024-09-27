package researchstack.data.repository.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.delay
import org.junit.jupiter.api.BeforeEach
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.data.datasource.sensor.SensorTracker
import researchstack.domain.model.TimestampMapData

internal abstract class SensorTrackerTestHelper<T : TimestampMapData> {
    protected val sensor = mockk<Sensor>()
    protected val sensorManager = mockk<SensorManager> {
        every { getDefaultSensor(any()) } returns sensor
    }

    protected val sensorEventListener = slot<SensorEventListener>()
    protected val healthDataOutPort = mockk<HealthDataOutPort>()

    abstract fun getTracker(): SensorTracker<T>

    @BeforeEach
    fun beforeEach() {
        justRun { sensorManager.unregisterListener(any<SensorEventListener>()) }
        every { sensorManager.registerListener(capture(sensorEventListener), any(), any()) } returns true
    }

    protected suspend fun generateSensorEvent(count: Int) {
        var sendEventCount = 0
        do {
            while (!sensorEventListener.isCaptured) {
                delay(10)
            }
            delay(1000)
            sensorEventListener.captured.let {
                it.onSensorChanged(createSensorEvent())
                if (sendEventCount == 3) {
                    it.onAccuracyChanged(sensor, 2)
                }
            }
            sendEventCount += 1
        } while (sendEventCount < count)
        delay(1000)
        getTracker().stopTracking()
    }

    abstract fun createSensorEvent(): SensorEvent
}
