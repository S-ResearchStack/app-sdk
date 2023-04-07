package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.view.common.SimpleTimerActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class GaitAndBalanceBMeasureView() : SimpleTimerActivityView<GaitAndBalanceBMeasureModel>() {
    @Composable
    override fun Render(
        model: GaitAndBalanceBMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun GaitAndBalanceBMeasurePreview() {
    val view = GaitAndBalanceBMeasureView()

    return view.Render(
        GaitAndBalanceBMeasureModel(
            id = "id",
            header = "Stand still for 20 seconds",
            timeSeconds = 20
        ),
        CallbackCollection(),
        null,
    )
}
