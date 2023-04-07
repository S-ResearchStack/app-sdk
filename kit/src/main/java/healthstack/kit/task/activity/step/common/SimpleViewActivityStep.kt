package healthstack.kit.task.activity.step.common

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.task.activity.view.common.SimpleActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class SimpleViewActivityStep(
    id: String,
    name: String,
    model: SimpleViewActivityModel,
    view: View<SimpleViewActivityModel> = SimpleActivityView(),
) : Step<SimpleViewActivityModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
