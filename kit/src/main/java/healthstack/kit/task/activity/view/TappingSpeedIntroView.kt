package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.activity.model.TappingSpeedIntroModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class TappingSpeedIntroView() : SimpleActivityView<TappingSpeedIntroModel>() {
    @Composable
    override fun Render(
        model: TappingSpeedIntroModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun TappingSpeedIntroPreview() {
    val view = TappingSpeedIntroView()
    val model = TappingSpeedIntroModel("id", "Tapping Speed")
    val callbackCollection = CallbackCollection()
    return view.Render(
        model,
        callbackCollection,
        null
    )
}
