package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.RangeOfMotionMeasureModel
import healthstack.kit.task.activity.view.RangeOfMotionMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class RangeOfMotionMeasureStep(
    id: String,
    name: String,
    model: RangeOfMotionMeasureModel,
    view: View<RangeOfMotionMeasureModel> = RangeOfMotionMeasureView(),
) : Step<RangeOfMotionMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
