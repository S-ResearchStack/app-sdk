package healthstack.kit.task.activity.model

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import healthstack.kit.R
import healthstack.kit.sensor.SensorUtils
import healthstack.kit.task.base.StepModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.math.sqrt

class ReactionTimeMeasureModel(
    id: String,
    title: String = "Reaction Time",
    val goal: String = "square",
    drawableId: Int? = null,
) : StepModel(id, title, drawableId) {
    enum class TestShape(val type: String, val drawableId: Int) {
        SQUARE("square", R.drawable.ic_activity_reaction_time_square),
        CIRCLE("circle", R.drawable.ic_activity_reaction_time_circle),
        TRIANGLE("triangle", R.drawable.ic_activity_reaction_time_triangle);

        companion object {
            fun getRandom(): TestShape {
                return values().random()
            }

            fun getNext(current: TestShape): TestShape {
                val values = TestShape.values()
                val next = (current.ordinal + (1..values.lastIndex).random()) % values.size
                return values[next]
            }
        }
    }

    class SensorDataManager() : SensorEventListener {
        private val shakeThreshold = 10f
        private val timeThreshold = 100

        private var lastRecorded: Long = 0
        private var acceleration = 10f
        private var currentAcc = SensorManager.GRAVITY_EARTH
        private var lastAcc = SensorManager.GRAVITY_EARTH

        private val eventChannel = Channel<Boolean>(Channel.CONFLATED)

        fun init() {
            SensorUtils.sensorManager.registerListener(
                this,
                SensorUtils.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        override fun onSensorChanged(event: SensorEvent) {
            val currentTime = System.currentTimeMillis()
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER && currentTime - lastRecorded > timeThreshold) {
                lastRecorded = currentTime
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                lastAcc = currentAcc
                currentAcc = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = currentAcc - lastAcc
                acceleration = acceleration * 0.9f + delta

                if (acceleration > shakeThreshold) {
                    eventChannel.trySend(true)
                }
            }
        }

        fun getData(): Flow<Boolean> {
            return eventChannel.receiveAsFlow()
        }

        fun cancel() {
            SensorUtils.sensorManager.unregisterListener(this)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private val dataManager = SensorDataManager()

    fun init() {
        dataManager.init()
    }

    fun getData(): Flow<Boolean> {
        return dataManager.getData()
    }

    fun close() {
        dataManager.cancel()
    }

    fun isGoal(shape: TestShape): Boolean {
        return shape.type == goal
    }
}
