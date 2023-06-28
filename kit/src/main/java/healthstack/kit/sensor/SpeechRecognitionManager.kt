package healthstack.kit.sensor

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import healthstack.kit.annotation.ForVerificationGenerated
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Timer
import kotlin.concurrent.timerTask

@ForVerificationGenerated
class SpeechRecognitionManager {
    companion object {
        private lateinit var INSTANCE: SpeechRecognitionManager
        lateinit var speechRecognizer: SpeechRecognizer
        lateinit var listener: SpeechRecognitionListener
        private lateinit var timer: Timer
        private lateinit var eventChannel: Channel<Int>

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = SpeechRecognitionManager()
                }
            }
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            SpeechRecognizer.createSpeechRecognizer(context)
        }

        fun initAmplitudeChannel() {
            eventChannel = Channel(Channel.UNLIMITED)
        }

        fun startListening(onResult: (String) -> Unit) {
            timer = Timer()

            listener = SpeechRecognitionListener(
                onResult, onFinish = { stopListening() }
            )
            speechRecognizer.setRecognitionListener(listener)

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            speechRecognizer.startListening(intent)

            timer.scheduleAtFixedRate(timerTask { addAmplitude() }, 0, 100)
        }

        private fun stopListening() {
            speechRecognizer.stopListening()
            timer.cancel()
            eventChannel.close()
        }

        private fun addAmplitude() {
            eventChannel.trySend(listener.currentAmplitude)
        }

        fun getAmplitudes(): Flow<Int> {
            return eventChannel.receiveAsFlow()
        }
    }
}
