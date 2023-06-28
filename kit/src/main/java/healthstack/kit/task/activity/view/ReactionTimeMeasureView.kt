package healthstack.kit.task.activity.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel.TestShape
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.util.ViewUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReactionTimeMeasureView : View<ReactionTimeMeasureModel>() {
    @Composable
    override fun Render(
        model: ReactionTimeMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val state = remember { mutableStateOf("ready") }
        val currentShape = remember { mutableStateOf(TestShape.getRandom()) }
        val countdown = remember { mutableStateOf(3) }
        val startTime = remember { mutableStateOf(0L) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                ViewUtil.SetSnackbar(
                    snackbarHostState = snackbarHostState,
                    onAction = { snackbarHostState.currentSnackbarData?.dismiss() }
                )
            },
            scaffoldState = scaffoldState,
            snackbarHost = { snackbarHostState },
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
                    text = "Shake phone when a ${model.goal} appears.",
                    style = AppTheme.typography.title2,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(198.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    when (state.value) {
                        "ready" -> {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 35.dp)
                                    .testTag("countdown"),
                                text = "Starting activity in ${countdown.value}...",
                                style = AppTheme.typography.body1,
                                color = AppTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                            )
                            LaunchedEffect(Unit) {
                                while (countdown.value > 0) {
                                    delay(1000)
                                    countdown.value -= 1
                                }
                                currentShape.value = TestShape.getRandom()
                                state.value = "measure"
                            }
                        }
                        "measure" -> {
                            DisposableEffect(state) {
                                model.init()

                                scope.launch {
                                    model.getData()
                                        .collectLatest {
                                            if (model.isGoal(currentShape.value)) {
                                                val endTime = System.currentTimeMillis()
                                                callbackCollection.setActivityResult(
                                                    "time(s)",
                                                    (endTime - startTime.value).toFloat() / 1000
                                                )
                                                callbackCollection.next()
                                            } else {
                                                state.value = "fail"
                                            }
                                        }
                                }
                                onDispose {
                                    model.close()
                                }
                            }

                            LaunchedEffect(currentShape.value) {
                                startTime.value = System.currentTimeMillis()
                                delay(3000)
                                if (model.isGoal(currentShape.value)) {
                                    state.value = "fail"
                                } else {
                                    currentShape.value = TestShape.getNext(currentShape.value)
                                }
                            }

                            Image(
                                painter = painterResource(currentShape.value.drawableId),
                                contentDescription = "reaction shape",
                                modifier = Modifier
                                    .size(90.dp)
                            )
                        }
                        else -> {
                            LaunchedEffect(snackbarHostState) {
                                snackbarHostState.showSnackbar(
                                    message = "Your attempt was not successful. Please try again.",
                                    actionLabel = "Dismiss",
                                    duration = SnackbarDuration.Short,
                                )
                                countdown.value = 3
                                state.value = "ready"
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun ReactionTimeMeasurePreview() {
    val view = ReactionTimeMeasureView()
    val model = ReactionTimeMeasureModel("id", "Reaction Time", "square", 0)
    val callbackCollection = CallbackCollection()
    return view.Render(
        model,
        callbackCollection,
        null
    )
}
