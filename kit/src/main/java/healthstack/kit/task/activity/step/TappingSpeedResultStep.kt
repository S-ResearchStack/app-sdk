package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.TappingSpeedResultModel
import healthstack.kit.task.activity.view.TappingSpeedResultView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class TappingSpeedResultStep(
    id: String,
    name: String,
    model: TappingSpeedResultModel,
    view: View<TappingSpeedResultModel> = TappingSpeedResultView(),
) : Step<TappingSpeedResultModel, Unit>(id, name, model, view, { }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
