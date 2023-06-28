package healthstack.kit.task.activity.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.sensor.SpeechRecognitionManager
import healthstack.kit.task.activity.model.SpeechRecognitionMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomRoundButton
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.Waveform
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

enum class SpeechRecognitionState(val state: String) {
    READY("Ready"),
    RECORDING("Recording"),
    FINISHED("Finished"),
}

class SpeechRecognitionMeasureView : View<SpeechRecognitionMeasureModel>() {

    @Composable
    override fun Render(
        model: SpeechRecognitionMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {

        val state = remember { mutableStateOf(SpeechRecognitionState.READY.state) }
        val speechText = remember { mutableStateOf("") }
        val amplitudes = remember { mutableStateListOf<Int>() }
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                BottomRoundButton(
                    when (state.value) {
                        SpeechRecognitionState.READY.state -> "Start Recording"
                        SpeechRecognitionState.RECORDING.state -> "Recording"
                        else -> "Complete Task"
                    },
                    state.value != SpeechRecognitionState.RECORDING.state
                ) {
                    when (state.value) {
                        SpeechRecognitionState.READY.state -> {
                            SpeechRecognitionManager.startListening { onResult: String ->
                                speechText.value = onResult
                                state.value = SpeechRecognitionState.FINISHED.state
                            }
                            state.value = SpeechRecognitionState.RECORDING.state
                        }
                        else -> {
                            callbackCollection.setActivityResult("result", speechText.value)
                            callbackCollection.next()
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(48.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Jaded zombies acted quaintly but kept driving their oxen forward.",
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(110.dp))

                if (state.value == SpeechRecognitionState.FINISHED.state) {
                    OutlinedTextField(
                        value = speechText.value,
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(152.dp)
                            .padding(horizontal = 24.dp),
                        textStyle = AppTheme.typography.body3,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppTheme.colors.onSurface,
                            disabledTextColor = AppTheme.colors.onSurface.copy(0.6F),
                            focusedBorderColor = AppTheme.colors.primary,
                            unfocusedBorderColor = AppTheme.colors.primary,
                            trailingIconColor = AppTheme.colors.primary
                        ),
                        readOnly = true,
                        singleLine = false,
                        shape = RoundedCornerShape(4.dp),
                    )
                } else {
                    LaunchedEffect(Unit) {
                        SpeechRecognitionManager.initAmplitudeChannel()
                        scope.launch {
                            SpeechRecognitionManager.getAmplitudes().collectLatest {
                                amplitudes.add(it)
                            }
                        }
                    }
                    Waveform(amplitudes = amplitudes)
                }
            }
        }
    }
}
