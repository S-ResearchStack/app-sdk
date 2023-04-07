package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.TappingSpeedMeasureModel
import healthstack.kit.task.activity.view.TappingSpeedMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class TappingSpeedMeasureStep(
    id: String,
    name: String,
    model: TappingSpeedMeasureModel,
    view: View<TappingSpeedMeasureModel> = TappingSpeedMeasureView(),
) : Step<TappingSpeedMeasureModel, Map<String, Int>>(id, name, model, view, { mapOf() }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
