package healthstack.kit.task.activity.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomRoundButton
import healthstack.kit.ui.TopBar
import kotlinx.coroutines.delay

class GuidedBreathingMeasureView : View<GuidedBreathingMeasureModel>() {
    @Composable
    override fun Render(
        model: GuidedBreathingMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        var start by remember { mutableStateOf(false) }
        var state by remember { mutableStateOf(false) }
        var curCycle by remember { mutableStateOf(0) }

        if (start) {
            LaunchedEffect(Unit) {
                for (i in 1..model.numCycle) {
                    state = false
                    delay(model.inhaleSecond * 1000)
                    state = !state
                    delay(model.exhaleSecond * 1000)
                    ++curCycle
                }

                callbackCollection.next()
            }
        }

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                if (!start) {
                    BottomRoundButton(
                        text = model.buttonText,
                    ) { start = true }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(26.dp))

                if (start) {
                    val dm = (if (state) model.exhaleSecond * 1000 else model.inhaleSecond * 1000).toInt()
                    Crossfade(targetState = state, animationSpec = tween(durationMillis = dm)) { targetState ->
                        Image(
                            painter =
                            painterResource(if (targetState) model.exhaleDrawableId else model.inhaleDrawableId),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.Center,
                        )
                    }
                } else {
                    model.readyDrawableId?.let { drawableId ->
                        Image(
                            painter = painterResource(drawableId),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.Center,
                        )
                    }
                }
                Spacer(modifier = Modifier.size(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            top = 80.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "You're inhaling",
                        style = if (!state) AppTheme.typography.title1 else AppTheme.typography.subtitle1,
                        color = if (!state) AppTheme.colors.primary else AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    Text(
                        text = "You're exhaling",
                        style = if (state) AppTheme.typography.title1 else AppTheme.typography.subtitle1,
                        color = if (state) AppTheme.colors.primary else AppTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            top = 20.dp
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
