package healthstack.kit.task.activity.model

import healthstack.kit.sensor.SensorType
import healthstack.kit.sensor.SensorType.ACCELEROMETER
import healthstack.kit.sensor.SensorType.GYROSCOPE
import healthstack.kit.task.activity.model.common.SimpleTimerActivityModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.util.InteractionType

class RangeOfMotionMeasureModel(
    id: String,
    title: String = "Range of Motion",
    header: String = "Right Arm Circumduction",
    body: List<String>? = listOf(
        "Place phone in your right hand.",
        "Straighten your right arm and move it in a full circle for 20 sec",
    ),
    textType: TextType = TextType.NUMBER,
    timeSeconds: Long = 20,
    autoFlip: Boolean = true,
    interactionType: InteractionType = InteractionType.VIBRATE,
    isRightHand: Boolean = true,
    dataPrefix: String = if (isRightHand) "right" else "left",
    sensors: List<SensorType> = listOf(GYROSCOPE, ACCELEROMETER),
) : SimpleTimerActivityModel(
    id, title, header, body, timeSeconds, textType, interactionType, autoFlip, dataPrefix, sensors
)
