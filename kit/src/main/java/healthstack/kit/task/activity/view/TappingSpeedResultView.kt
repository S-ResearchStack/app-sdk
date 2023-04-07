package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.TappingSpeedResultModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class TappingSpeedResultView : SimpleActivityView<TappingSpeedResultModel>() {
    @Composable
    override fun Render(
        model: TappingSpeedResultModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun TappingSpeedResultPreview() {
    val view = TappingSpeedResultView()
    val model = TappingSpeedResultModel("id", "Tapping Speed")
    val callbackCollection = object : CallbackCollection() {}
    return view.Render(
        model,
        callbackCollection,
        null
    )
}
