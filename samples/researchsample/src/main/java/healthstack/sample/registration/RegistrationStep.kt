package healthstack.sample.registration

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class RegistrationStep(
    model: RegistrationModel,
    view: View<RegistrationModel> = RegistrationView(),
) : Step<RegistrationModel, Boolean>("", "", model, view, { true }) {

    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model = model, callbackCollection = callbackCollection, holder = null)
    }
}
