package healthstack.kit.task.activity.model

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import healthstack.kit.R
import healthstack.kit.task.base.StepModel

class GuidedBreathingMeasureModel(
    id: String,
    title: String = "Guided Breathing",
    val numCycle: Int = 3,
    inhaleSecond: Int = 5,
    exhaleSecond: Int = 5,
) : StepModel(id, title, null) {
    enum class BreathingState(val drawableId: Int, val text: String, val pause: Long) {
        READY(R.drawable.ic_activity_breathing_ready, "Ready", 0),
        INHALE(R.drawable.ic_activity_breathing_inhale, "Inhale", 100),
        EXHALE(R.drawable.ic_activity_breathing_exhale, "Exhale", 100);

        companion object {
            fun getNext(current: BreathingState): BreathingState {
                return if (current == INHALE) EXHALE else INHALE
            }
        }
    }

    val startAnim = tween<Float>(
        300,
        0,
        CubicBezierEasing(0f, 0f, 0.58f, 1f)
    )

    val inhaleAnim = tween<Dp>(
        inhaleSecond * 1000,
        600,
        CubicBezierEasing(0f, 0f, 0.58f, 1f)
    )

    val exhaleAnim = tween<Dp>(
        exhaleSecond * 1000,
        600,
        CubicBezierEasing(0f, 0f, 0.58f, 1f)
    )

    val cycleAnim = tween<Float>(
        300,
        0,
        CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
    )
}
