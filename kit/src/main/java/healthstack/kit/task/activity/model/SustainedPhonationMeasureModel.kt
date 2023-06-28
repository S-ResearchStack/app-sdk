package healthstack.kit.task.activity.model

import android.content.Context
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.Dp
import healthstack.kit.R
import healthstack.kit.sensor.AudioRecorder
import healthstack.kit.task.base.StepModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class SustainedPhonationMeasureModel(
    id: String,
    title: String = "Sustained Phonation",
    drawableId: Int? = null,
    val noiseThreshold: Int = 3000,
    inhaleSecond: Int = 5,
    exhaleSecond: Int = 5,
    val recorder: AudioRecorder.Companion = AudioRecorder,
) : StepModel(id, title, drawableId) {
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

    var filePath = ""
    fun start(context: Context) {
        val externalPath = context.externalCacheDir?.absolutePath
        filePath = "$externalPath/${title}_${LocalDateTime.now()}.m4a"
        recorder.startRecording(filePath)
    }

    fun getAmplitudes(): Flow<Int> {
        return recorder.getAmplitudes()
    }

    fun stop() {
        recorder.stopRecording()
    }

    fun delete() {
        recorder.discardRecording(filePath)
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
