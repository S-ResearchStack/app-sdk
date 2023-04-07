package healthstack.kit.sensor

import android.content.Context
import android.hardware.SensorManager

class SensorUtils private constructor(
    context: Context,
) {
    companion object {
        private lateinit var INSTANCE: SensorUtils
        lateinit var sensorManager: SensorManager

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = SensorUtils(context)
                }
            }
        }

        fun getInstance(): SensorUtils = INSTANCE
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
}
