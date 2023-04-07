package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel
import healthstack.kit.task.activity.view.ReactionTimeMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class ReactionTimeMeasureStep(
    id: String,
    name: String,
    model: ReactionTimeMeasureModel,
    view: View<ReactionTimeMeasureModel> = ReactionTimeMeasureView(),
) : Step<ReactionTimeMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
