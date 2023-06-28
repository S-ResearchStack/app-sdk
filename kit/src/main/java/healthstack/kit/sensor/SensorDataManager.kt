package healthstack.kit.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import healthstack.kit.annotation.ForVerificationGenerated
import healthstack.kit.sensor.SensorType.ACCELEROMETER
import healthstack.kit.sensor.SensorType.GYROSCOPE

enum class SensorType(val id: String) {
    ACCELEROMETER("accelerometer"), GYROSCOPE("gyroscope")
}

class SensorDataManager(
    private val sensorTypes: List<SensorType>,
) : SensorEventListener {
    var times = mutableListOf<Long>()
    var ret: MutableMap<String, List<MutableList<Float>>> = mutableMapOf()

    fun init() {
        sensorTypes.forEach {
            when (it) {
                ACCELEROMETER -> {
                    SensorUtils.sensorManager.registerListener(
                        this,
                        SensorUtils.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }

                GYROSCOPE -> {
                    SensorUtils.sensorManager.registerListener(
                        this,
                        SensorUtils.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                }
            }
        }
    }

    fun close() {
        SensorUtils.sensorManager.unregisterListener(this)
    }

    private fun addResponse(key: String, values: FloatArray) {
        if (ret.containsKey(key)) {
            for (i in 0..2)
                ret[key]!![i].add(values[i])
        } else {
            ret[key] = listOf(
                mutableListOf(values[0]), mutableListOf(values[1]),
                mutableListOf(values[2])
            )
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                times.add(System.currentTimeMillis())
                addResponse(ACCELEROMETER.id, event.values)
            }

            Sensor.TYPE_GYROSCOPE -> {
                times.add(System.currentTimeMillis())
                addResponse(GYROSCOPE.id, event.values)
            }

            else -> @ForVerificationGenerated {
                TODO("Not yet implemented")
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
