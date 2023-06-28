package healthstack.kit.sensor

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.io.File
import java.io.IOException
import java.util.Timer
import kotlin.concurrent.timerTask

class AudioRecorder private constructor(
    val context: Context,
) {
    companion object {
        private lateinit var INSTANCE: AudioRecorder
        private lateinit var recorder: MediaRecorder
        private lateinit var timer: Timer
        private lateinit var eventChannel: Channel<Int>

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = AudioRecorder(context)
                }
            }
        }

        fun getInstance(): AudioRecorder = INSTANCE

        fun startRecording(filePath: String) {
            timer = Timer()
            eventChannel = Channel(Channel.UNLIMITED)

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(705600)
                setAudioSamplingRate(44100)
                setOutputFile(filePath)

                try {
                    prepare()
                } catch (e: IOException) {
                    Log.e("AudioRecorder", "prepare() failed")
                }
                start()
            }
            timer.scheduleAtFixedRate(timerTask { addAmplitude() }, 0, 100)
            timer.scheduleAtFixedRate(timerTask { showTransition() }, 50, 100)
        }

        private fun showTransition() {
            eventChannel.trySend(-1)
        }

        private fun addAmplitude() {
            eventChannel.trySend(recorder.maxAmplitude)
        }

        fun getAmplitudes(): Flow<Int> {
            return eventChannel.receiveAsFlow()
        }

        fun stopRecording() {
            recorder.apply {
                stop()
                reset()
            }
            timer.cancel()
            eventChannel.close()
        }

        fun discardRecording(filePath: String) {
            stopRecording()
            val tempFile = File(filePath)
            tempFile.delete()
        }
    }

    init {
        recorder = MediaRecorder()
    }
}
