package healthstack.kit.task.activity.model

import android.content.Context
import healthstack.kit.sensor.AudioRecorder
import healthstack.kit.task.base.StepModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MobileSpirometryMeasureModel(
    id: String,
    title: String = "Mobile Spirometry",
    val count: Int = 3,
    drawableId: Int? = null,
    val buttonText: String = "Stop Recording",
    val recorder: AudioRecorder.Companion = AudioRecorder,
) : StepModel(id, title, drawableId) {
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
}
