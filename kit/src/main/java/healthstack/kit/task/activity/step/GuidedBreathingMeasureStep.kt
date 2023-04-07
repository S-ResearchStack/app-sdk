package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel
import healthstack.kit.task.activity.view.GuidedBreathingMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class GuidedBreathingMeasureStep(
    id: String,
    name: String,
    model: GuidedBreathingMeasureModel,
    view: View<GuidedBreathingMeasureModel> = GuidedBreathingMeasureView(),
) : Step<GuidedBreathingMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
