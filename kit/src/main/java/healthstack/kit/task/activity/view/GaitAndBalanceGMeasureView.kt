package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.R
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.GaitAndBalanceGMeasureModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class GaitAndBalanceGMeasureView() : SimpleActivityView<GaitAndBalanceGMeasureModel>() {
    @Composable
    override fun Render(
        model: GaitAndBalanceGMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun GaitAndBalanceMeasurePreview() {
    val view = GaitAndBalanceGMeasureView()

    return view.Render(
        GaitAndBalanceGMeasureModel(
            id = "id",
            header = "Walk in a straight line unassisted for 20 steps",
            buttonText = "Done"
        ),
        CallbackCollection(),
        null,
    )
}

@PreviewGenerated
@Preview(showBackground = true, widthDp = 360, heightDp = 700)
@Composable
fun GaitAndBalanceMeasurePreview2() {
    val view = GaitAndBalanceGMeasureView()

    return view.Render(
        GaitAndBalanceGMeasureModel(
            id = "id",
            drawableId = R.drawable.ic_activity_gait_and_balance_back,
            header = "Turn around and walk back to your starting point",
            buttonText = "Done"
        ),
        CallbackCollection(),
        null,
    )
}
