package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.base.StepModel

class GuidedBreathingMeasureModel(
    id: String,
    title: String = "Guided Breathing",
    val buttonText: String = "Start Recording",
    val readyDrawableId: Int = R.drawable.ic_activity_guided_breathing_ready,
    val inhaleDrawableId: Int = R.drawable.ic_activity_guided_breathing_inhale,
    val exhaleDrawableId: Int = R.drawable.ic_activity_guided_breathing_exhale,
    val numCycle: Int = 10,
    val inhaleSecond: Long = 3,
    val exhaleSecond: Long = 9,
) : StepModel(id, title, null)
