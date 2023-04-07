package healthstack.kit.task.activity.view.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.BottomRoundButton
import healthstack.kit.ui.ListedText
import healthstack.kit.ui.TextType.BULLET
import healthstack.kit.ui.TextType.NUMBER
import healthstack.kit.ui.TopBar

open class SimpleActivityView<T : SimpleViewActivityModel> : View<T>() {
    @Composable
    override fun Render(
        model: T,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopBar(model.title) {
                    callbackCollection.prev()
                }
            },
            bottomBar = {
                if (!model.buttonText.isNullOrEmpty())
                    BottomRoundButton(model.buttonText) {
                        callbackCollection.next()
                    }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(56.dp))

                model.drawableId?.let {
                    Image(
                        modifier = Modifier
                            .size(250.dp),
                        painter = painterResource(it),
                        contentDescription = "image for view"
                    )
                }

                Spacer(Modifier.height(54.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    text = model.header,
                    style = AppTheme.typography.headline3,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(32.dp))

                model.body?.let {
                    ListedText(it, model.textType)
                }
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun SimpleViewButtonPreview() {
    val view = SimpleActivityView<GaitAndBalanceIntroModel>()

    return view.Render(
        GaitAndBalanceIntroModel(
            "id",
            buttonText = "Begin"
        ),
        CallbackCollection(),
        null
    )
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun SimpleViewNoButtonPreview() {
    val view = SimpleActivityView<GaitAndBalanceIntroModel>()

    return view.Render(
        GaitAndBalanceIntroModel(
            "id",
            "title",
            header = "custom header",
            body = listOf(
                "Walk unassisted for 20 steps in a straight line.",
                "Turn around and walk back to your starting point.\n",
                "Stand still for 20 seconds."
            ),
            textType = NUMBER
        ),
        CallbackCollection(),
        null
    )
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun SimpleViewNoButtonBulletPreview() {
    val view = SimpleActivityView<GaitAndBalanceIntroModel>()

    return view.Render(
        GaitAndBalanceIntroModel(
            "id",
            "title",
            header = "custom header",
            body = listOf(
                "Walk unassisted for 20 steps in a straight line.",
                "Turn around and walk back to your starting point.\n",
                "Stand still for 20 seconds."
            ),
            textType = BULLET
        ),
        CallbackCollection(),
        null
    )
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun SimpleViewResultPreview() {
    val view = SimpleActivityView<GaitAndBalanceResultModel>()

    return view.Render(
        GaitAndBalanceResultModel(
            id = "id"
        ),
        CallbackCollection(),
        null
    )
}
