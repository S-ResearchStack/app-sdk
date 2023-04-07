package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class GaitAndBalanceResultView() : SimpleActivityView<GaitAndBalanceResultModel>() {
    @Composable
    override fun Render(
        model: GaitAndBalanceResultModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Composable
fun GaitAndBalanceResultPreview() {
    val view = GaitAndBalanceResultView()

    return view.Render(
        GaitAndBalanceResultModel(
            id = "id"
        ),
        CallbackCollection(),
        null
    )
}
