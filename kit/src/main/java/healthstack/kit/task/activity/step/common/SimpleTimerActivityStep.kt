package healthstack.kit.task.activity.step.common

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.common.SimpleTimerActivityModel
import healthstack.kit.task.activity.view.common.SimpleTimerActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class SimpleTimerActivityStep(
    id: String,
    name: String,
    model: SimpleTimerActivityModel,
    view: View<SimpleTimerActivityModel> = SimpleTimerActivityView(),
) : Step<SimpleTimerActivityModel, Unit>(id, name, model, view, { }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
