package healthstack.kit.sensor

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import healthstack.kit.annotation.ForVerificationGenerated

@ForVerificationGenerated
class SpeechRecognitionListener(
    private val onResult: (String) -> Unit,
    private val onFinish: () -> Unit,
    var currentAmplitude: Int = 0,
) : RecognitionListener {

    override fun onReadyForSpeech(params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onRmsChanged(rmsdB: Float) {
        currentAmplitude = rmsdB.times(1000).toInt()
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onEndOfSpeech() {
        onResult("")
        onFinish()
    }

    override fun onError(error: Int) {
    }

    override fun onResults(results: Bundle) {
        val text = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0) ?: ""
        onResult(text)
        onFinish()
    }

    override fun onPartialResults(partialResults: Bundle) {
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }
}
