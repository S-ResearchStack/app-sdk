package researchstack.data.datasource.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.sensor.TrackerDataType

abstract class SensorTracker<T : TimestampMapData>(
    private val sensorManager: SensorManager,
    sensorType: Int,
    dataStore: DataStore<Preferences>,
) : BaseTracker<T>(dataStore) {
    abstract override val trackerDataType: TrackerDataType
    private var sensor = sensorManager.getDefaultSensor(sensorType)
    private lateinit var sensorEventListener: SensorEventListener

    abstract fun SensorEvent.toModel(accuracy: Int): T

    override fun startTracking(): Flow<T> {
        if (isTracking) return dataFlow
        isTracking = true

        dataFlow = callbackFlow {
            sensorEventListener = object : SensorEventListener {
                private var accuracy: Int? = null
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    trySend(
                        sensorEvent.toModel(accuracy ?: SensorManager.SENSOR_STATUS_UNRELIABLE)
                    )
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    this.accuracy = accuracy
                }
            }

            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            sendChannel = channel

            awaitClose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }.flowOn(Dispatchers.IO)

        return dataFlow
    }
}
