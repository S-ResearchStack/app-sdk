package healthstack.kit.task.onboarding.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.ConsentTextModel
import healthstack.kit.task.onboarding.view.ConsentTextView

class ConsentTextStep(
    id: String,
    name: String,
    model: ConsentTextModel,
    view: View<ConsentTextModel> = ConsentTextView(),
) : Step<ConsentTextModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) =
        view.Render(model, callbackCollection, null)
}
