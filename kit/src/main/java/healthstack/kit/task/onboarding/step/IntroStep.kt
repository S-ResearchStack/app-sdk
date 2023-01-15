package healthstack.kit.task.onboarding.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.IntroModel
import healthstack.kit.task.onboarding.view.IntroView

open class IntroStep(
    id: String,
    name: String,
    model: IntroModel,
    view: View<IntroModel> = IntroView("Get Started"),
) : Step<IntroModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection) {
        view.Render(model, callbackCollection, null)
    }
}
