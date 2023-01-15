package healthstack.kit.task.signup.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.signup.model.SignUpModel
import healthstack.kit.task.signup.view.SignUpView

open class SignUpStep(
    id: String,
    name: String,
    model: SignUpModel,
    view: View<SignUpModel> = SignUpView(),
) : Step<SignUpModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
