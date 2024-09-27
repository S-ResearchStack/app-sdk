package researchstack.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.OutputFormat
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import researchstack.BuildConfig
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Timer
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.timerTask

class AudioRecorder(
    val context: Context,
    audioSource: Int = MediaRecorder.AudioSource.DEFAULT,
    outputFormat: Int = OutputFormat.DEFAULT,
    audioEncoder: Int = AudioEncoder.DEFAULT,
    private val ampPeriod: Long = 66,
    private val maxAmpSize: Int = 500,
) {
    private var start = AtomicBoolean(false)
    private val outputDir = "${Environment.getExternalStorageDirectory()}/${BuildConfig.FOLDER_NAME}/recorder"
    private val timer: Timer
    private val recorder: MediaRecorder

    private val _amplitudes = MutableStateFlow(listOf<Int>())
    val amplitudes: StateFlow<List<Int>> = _amplitudes

    private fun addAmplitude(value: Int) {
        _amplitudes.value = _amplitudes.value.toMutableList().apply {
            add(value)
            if (size > maxAmpSize) {
                removeFirst()
            }
        }
    }

    init {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            throw Exception("no audio permission")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            throw Exception("no all-files access permission")
        }

        recorder = MediaRecorder().apply {
            setAudioSource(audioSource)
            setOutputFormat(outputFormat)
            setAudioEncoder(audioEncoder)
        }

        timer = Timer()

        Files.createDirectories(Paths.get(outputDir))
    }

    fun startRecording(): String {
        if (!start.compareAndSet(false, true)) return ""

        val filePath = "$outputDir/${System.currentTimeMillis()}-${UUID.randomUUID()}.mp3"

        recorder.apply {
            setOutputFile(filePath)
            prepare()
            start()
        }

        timer.scheduleAtFixedRate(timerTask { addAmplitude(recorder.maxAmplitude) }, 0, ampPeriod)

        return filePath
    }

    fun stopRecording() {
        start.set(false)
        timer.cancel()
        recorder.apply {
            stop()
            release()
        }
    }
}
