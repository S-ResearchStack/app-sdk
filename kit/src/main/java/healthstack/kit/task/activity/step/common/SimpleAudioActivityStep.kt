package healthstack.kit.task.activity.step.common

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.common.SimpleAudioActivityModel
import healthstack.kit.task.activity.view.common.SimpleAudioActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class SimpleAudioActivityStep(
    id: String,
    name: String,
    model: SimpleAudioActivityModel,
    view: View<SimpleAudioActivityModel> = SimpleAudioActivityView(),
) : Step<SimpleAudioActivityModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
