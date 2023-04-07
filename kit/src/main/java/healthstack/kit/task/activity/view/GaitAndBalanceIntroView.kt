package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.ui.TextType.NUMBER

class GaitAndBalanceIntroView() : SimpleActivityView<GaitAndBalanceIntroModel>() {
    @Composable
    override fun Render(
        model: GaitAndBalanceIntroModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun GaitAndBalanceIntroPreview() {
    val view = GaitAndBalanceIntroView()

    return view.Render(
        GaitAndBalanceIntroModel(
            id = "id",
            textType = NUMBER,
            body = listOf(
                "Walk unassisted for 20 steps in a straight line.",
                "Turn around and walk back to your starting point.\n",
                "Stand still for 20 seconds."
            ),
            buttonText = "Begin"
        ),
        CallbackCollection(),
        null,
    )
}
