package healthstack.kit.task.activity.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.task.activity.model.MobileSpirometryMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomRoundButton
import healthstack.kit.ui.ListedText
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.Waveform
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MobileSpirometryMeasureView : View<MobileSpirometryMeasureModel>() {
    @Composable
    override fun Render(
        model: MobileSpirometryMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val context = LocalContext.current
        val amplitudes = remember { mutableStateListOf<Int>() }
        val transition = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    model.delete()
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                BottomRoundButton(model.buttonText) {
                    model.stop()
                    callbackCollection.setActivityResult("recording", model.filePath)
                    callbackCollection.setActivityResult("counts", model.count)
                    callbackCollection.next()
                }
            }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LaunchedEffect(Unit) {
                    model.start(context)
                    scope.launch {
                        model.getAmplitudes()
                            .collectLatest {
                                if (it < 0) {
                                    transition.value = true
                                } else {
                                    amplitudes.add(it)
                                    transition.value = false
                                }
                            }
                    }
                }
                Spacer(Modifier.height(136.dp))

                Waveform(amplitudes = amplitudes, move = transition.value)

                Spacer(Modifier.height(64.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = "Breathe Forcefully ${model.count} Times",
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(32.dp))

                ListedText(
                    listOf(
                        "Hold phone 6 inches from mouth.",
                        "Take a deep breath and blow out fast, forcefully and as long as you can.",
                        "Repeat ${model.count} times in one recording."
                    ),
                    TextType.NUMBER
                )
            }
        }
    }
}
