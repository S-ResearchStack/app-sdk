package healthstack.kit.task.activity.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.task.activity.model.SustainedPhonationMeasureModel
import healthstack.kit.task.activity.model.SustainedPhonationMeasureModel.BreathingState
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.AlertPopup
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.Waveform
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SustainedPhonationMeasureView : View<SustainedPhonationMeasureModel>() {
    @Composable
    override fun Render(
        model: SustainedPhonationMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        var noiseCheck by remember { mutableStateOf("standby") }
        var state by remember { mutableStateOf(BreathingState.READY) }
        var countdown by remember { mutableStateOf(3) }
        val currentSize = animateDpAsState(
            targetValue = if (state == BreathingState.INHALE) 244.dp else 168.dp,
            animationSpec = if (state == BreathingState.INHALE) model.inhaleAnim else model.exhaleAnim
        ) {
            scope.launch {
                delay(state.pause)
                if (state == BreathingState.EXHALE) {
                    model.stop()
                    callbackCollection.setActivityResult("recording", model.filePath)
                    callbackCollection.next()
                }
                state = BreathingState.getNext(state)
            }
        }
        val amplitudes = remember { mutableStateListOf<Int>() }
        var transition by remember { mutableStateOf(false) }

        when (noiseCheck) {
            "standby" -> {
                LaunchedEffect(Unit) {
                    model.start(context)
                    scope.launch {
                        model.getAmplitudes()
                            .collectLatest {
                                if (it > model.noiseThreshold) {
                                    model.delete()
                                    noiseCheck = "fail"
                                }
                            }
                    }
                    delay(3000)
                    model.delete()
                    noiseCheck = "pass"
                }
            }

            "pass" -> {
                LaunchedEffect(Unit) {
                    while (countdown > 0) {
                        delay(1000)
                        countdown--
                    }
                    state = BreathingState.getNext(state)
                }
            }

            else -> {
                AlertPopup(
                    initiateText = null,
                    title = "Noise Level",
                    body = "The noise level is too high. Please move to a quieter environment to proceed.",
                    confirmText = "Resume Task",
                    dismissText = null,
                    onDismissRequest = { callbackCollection.prev() },
                    onConfirmClicked = { noiseCheck = "standby" }
                )
            }
        }

        if (state == BreathingState.EXHALE) {
            LaunchedEffect(Unit) {
                model.start(context)
                scope.launch {
                    model.getAmplitudes()
                        .collectLatest {
                            if (it < 0) {
                                transition = true
                            } else {
                                amplitudes.add(it)
                                transition = false
                            }
                        }
                }
            }
        }

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    if (state == BreathingState.EXHALE) model.delete()
                    callbackCollection.prev()
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
                    text = if (noiseCheck != "pass") "Checking background noise level..."
                    else if (state == BreathingState.READY) "Starting activity in $countdown..."
                    else "Inhale, then exhale with a loud \"ahh\".",
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(54.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(254.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (noiseCheck == "pass") {
                        Crossfade(
                            targetState = state,
                            animationSpec = if (state == BreathingState.READY)
                                model.startAnim else model.cycleAnim
                        ) { targetState ->
                            Image(
                                painter = painterResource(targetState.drawableId),
                                contentDescription = "breathing guide",
                                modifier = Modifier
                                    .size(currentSize.value)
                                    .align(Alignment.Center),
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                                text = targetState.text,
                                style = AppTheme.typography.title1,
                                color = AppTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                            )
                        }
                    } else {
                        Image(
                            painter = painterResource(state.drawableId),
                            contentDescription = "breathing guide",
                            modifier = Modifier
                                .size(currentSize.value)
                                .align(Alignment.Center),
                        )
                    }
                }
                Waveform(amplitudes = amplitudes, move = transition)
            }
        }
    }
}
