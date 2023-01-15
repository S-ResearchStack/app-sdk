package healthstack.kit.task.onboarding.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.EligibilityIntroModel
import healthstack.kit.task.onboarding.view.EligibilityIntroView

open class EligibilityIntroStep(
    id: String,
    name: String,
    model: EligibilityIntroModel,
    view: View<EligibilityIntroModel> = EligibilityIntroView(),
) : Step<EligibilityIntroModel, Boolean>(id, name, model, view, { true }) {

    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
