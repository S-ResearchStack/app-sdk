package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.MobileSpirometryMeasureModel
import healthstack.kit.task.activity.view.MobileSpirometryMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class MobileSpirometryMeasureStep(
    id: String,
    name: String,
    model: MobileSpirometryMeasureModel,
    view: View<MobileSpirometryMeasureModel> = MobileSpirometryMeasureView(),
) : Step<MobileSpirometryMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
