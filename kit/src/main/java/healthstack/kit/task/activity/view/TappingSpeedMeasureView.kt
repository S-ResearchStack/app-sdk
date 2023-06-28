package healthstack.kit.task.activity.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.TappingSpeedMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.MinuteTextTimer
import healthstack.kit.ui.TopBar
import healthstack.kit.ui.util.InteractionType.VIBRATE
import healthstack.kit.ui.util.InteractionUtil

class TappingSpeedMeasureView() : View<TappingSpeedMeasureModel>() {
    @Composable
    override fun Render(
        model: TappingSpeedMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val tapCount = remember { mutableStateOf(0) }
        val composeContext = LocalContext.current

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(56.dp))

                MinuteTextTimer {
                    callbackCollection.setActivityResult(
                        model.handType, tapCount.value.toString()
                    )
                    InteractionUtil.feedback(composeContext, VIBRATE)
                    callbackCollection.next()
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    text = "Tap the buttons with your ${model.handType} hand",
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(166.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 62.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TappingButton(tapCount)
                    Spacer(modifier = Modifier.width(56.dp))
                    TappingButton(tapCount)
                }
            }
        }
    }
}

@Composable
fun TappingButton(tapCount: MutableState<Int>) = Image(
    painter = painterResource(R.drawable.ic_tapping_button),
    contentDescription = "tapping icon",
    modifier = Modifier
        .clickable(
            enabled = true,
            onClick = { tapCount.value += 1 }
        )
)

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun TappingSpeedMeasurePreview() {
    val view = TappingSpeedMeasureView()
    val model = TappingSpeedMeasureModel("id", "Tapping Speed")
    val callbackCollection = CallbackCollection()
    return view.Render(
        model,
        callbackCollection,
        null
    )
}
