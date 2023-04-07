package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.TappingSpeedIntroModel
import healthstack.kit.task.activity.view.TappingSpeedIntroView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class TappingSpeedIntroStep(
    id: String,
    name: String,
    model: TappingSpeedIntroModel,
    view: View<TappingSpeedIntroModel> = TappingSpeedIntroView(),
) : Step<TappingSpeedIntroModel, Unit>(id, name, model, view, { }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
