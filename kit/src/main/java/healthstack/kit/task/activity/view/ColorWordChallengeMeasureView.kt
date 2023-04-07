package healthstack.kit.task.activity.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import healthstack.kit.task.activity.model.ColorWordChallengeMeasureModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.TopBar

class ColorWordChallengeMeasureView : View<ColorWordChallengeMeasureModel>() {
    @Composable
    override fun Render(
        model: ColorWordChallengeMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val startTime = System.currentTimeMillis()
        var state by remember { mutableStateOf(0) }
        var cur by remember { mutableStateOf(0) }
        var textColor by remember { mutableStateOf(model.getRandomColor()) }
        val testset = model.getTestset()

        val onClick: (Int) -> Unit = func@{ idx ->
            if (testset[cur][0] == idx) state = 1
            else {
                state = 2
                return@func
            }

            cur += 1
            textColor = model.getRandomColor()

            if (cur == model.numTest) {
                val endTime = System.currentTimeMillis()
                callbackCollection.setActivityResult("time(ms)", endTime - startTime)
                callbackCollection.next()
            }
        }

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            },
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(26.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 56.dp),
                    text = when (state) {
                        1 -> "Correct"
                        2 -> "Incorrect"
                        else -> "Pick the color of the word"
                    },
                    style = AppTheme.typography.body1,
                    color = when (state) {
                        1 -> Color.Green
                        2 -> Color.Red
                        else -> AppTheme.colors.onSurface
                    },
                    textAlign = TextAlign.Center,
                )

                Box(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = model.colorWords[testset[cur][testset[cur][0]]],
                        style = AppTheme.typography.headline2,
                        color = Color(textColor),
                        textAlign = TextAlign.Center,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Circle(Color(model.colorCodes[testset[cur][1]])) { onClick(1) }
                    Circle(Color(model.colorCodes[testset[cur][2]])) { onClick(2) }
                    Circle(Color(model.colorCodes[testset[cur][3]])) { onClick(3) }
                    Circle(Color(model.colorCodes[testset[cur][4]])) { onClick(4) }
                }
            }
        }
    }
}

@Composable
fun Circle(
    color: Color = Color.Red,
    onClick: () -> Unit = {},
) {
    Column(modifier = Modifier.wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(color).clickable { onClick() }
        )
    }
}
