package researchstack.presentation.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import researchstack.domain.model.events.WearableOffBody
import researchstack.util.toEpochMilli
import java.time.LocalDateTime
import javax.inject.Inject

class WearableSensorManager @Inject constructor(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val mSensor: Sensor = sensorManager.getDefaultSensor(0x1101B)!!
    private lateinit var sensorEventListener: SensorEventListener
    private var sendChannelSensorChange: SendChannel<WearableOffBody>? = null

    fun startTracking(): Flow<WearableOffBody> {
        val dataFlow = callbackFlow {
            sensorEventListener = object : SensorEventListener {
                private var accuracy: Int? = null
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    trySend(
                        WearableOffBody(
                            timestamp = LocalDateTime.now().toEpochMilli(),
                            isWearableOffBody = sensorEvent.values[0].toInt()
                        )
                    )
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                    this.accuracy = accuracy
                }
            }

            sensorManager.registerListener(
                sensorEventListener,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )

            sendChannelSensorChange = channel

            awaitClose {
                sensorManager.unregisterListener(sensorEventListener)
            }
        }.flowOn(Dispatchers.IO)
        return dataFlow
    }

    fun stopTracking() {
        sendChannelSensorChange?.close()
        sensorManager.unregisterListener(sensorEventListener)
    }
}
