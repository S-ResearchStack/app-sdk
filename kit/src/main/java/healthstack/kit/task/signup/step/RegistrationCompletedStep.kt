package healthstack.kit.task.signup.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.signup.model.RegistrationCompletedModel
import healthstack.kit.task.signup.view.RegistrationCompletedView

class RegistrationCompletedStep(
    id: String,
    name: String,
    model: RegistrationCompletedModel,
    view: View<RegistrationCompletedModel> = RegistrationCompletedView(),
) : Step<RegistrationCompletedModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
