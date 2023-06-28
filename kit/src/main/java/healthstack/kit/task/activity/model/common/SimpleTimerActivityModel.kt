package healthstack.kit.task.activity.model.common

import healthstack.kit.sensor.SensorDataManager
import healthstack.kit.sensor.SensorType
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TextType.PARAGRAPH
import healthstack.kit.ui.util.InteractionType
import healthstack.kit.ui.util.InteractionType.NOTHING

open class SimpleTimerActivityModel(
    id: String,
    title: String,
    val header: String,
    val body: List<String>? = null,
    val timeSeconds: Long = 10,
    val textType: TextType = PARAGRAPH,
    val interactionType: InteractionType = NOTHING,
    val autoFlip: Boolean = false,
    val dataPrefix: String,
    val sensors: List<SensorType>,
) : StepModel(id, title, null) {

    val accelerometer: List<MutableList<Float>>?
        get() = dataManager.ret[SensorType.ACCELEROMETER.id]

    val gyroscope: List<MutableList<Float>>?
        get() = dataManager.ret[SensorType.GYROSCOPE.id]

    val timeMillis: List<Long>
        get() = dataManager.times

    fun init() {
        dataManager.init()
    }

    fun close() {
        dataManager.close()
    }

    private val dataManager = SensorDataManager(
        sensors
    )
}
