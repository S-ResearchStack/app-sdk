package healthstack.kit.task.activity.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel.BreathingState
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GuidedBreathingMeasureView : View<GuidedBreathingMeasureModel>() {
    @Composable
    override fun Render(
        model: GuidedBreathingMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val scope = rememberCoroutineScope()
        var state by remember { mutableStateOf(BreathingState.READY) }
        var curCycle by remember { mutableStateOf(0) }
        val currentSize = animateDpAsState(
            targetValue = if (state == BreathingState.INHALE) 244.dp else 168.dp,
            animationSpec = if (state == BreathingState.INHALE) model.inhaleAnim else model.exhaleAnim
        ) {
            scope.launch {
                delay(state.pause)
                if (state == BreathingState.INHALE) {
                    curCycle++
                } else if (curCycle == model.numCycle) {
                    callbackCollection.next()
                }
                state = BreathingState.getNext(state)
            }
        }

        LaunchedEffect(Unit) {
            delay(1000)
            state = BreathingState.getNext(state)
        }

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(362.dp),
                    contentAlignment = Alignment.Center
                ) {
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
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            top = 25.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "You're inhaling",
                        style = if (state == BreathingState.INHALE) AppTheme.typography.title1 else
                            AppTheme.typography.subtitle1,
                        color = if (state == BreathingState.INHALE) AppTheme.colors.primary else
                            AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = "You're exhaling",
                        style = if (state == BreathingState.EXHALE) AppTheme.typography.title1 else
                            AppTheme.typography.subtitle1,
                        color = if (state == BreathingState.EXHALE) AppTheme.colors.primary else
                            AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            top = 24.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .absolutePadding(
                                top = 0.dp
                            ),
                        text = "$curCycle/${model.numCycle}",
                        style = AppTheme.typography.headline2,
                        color = AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .absolutePadding(
                                top = 0.dp,
                                left = 4.dp,
                                bottom = 4.dp,
                            ),
                        text = "cycles",
                        style = AppTheme.typography.title1,
                        color = AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
