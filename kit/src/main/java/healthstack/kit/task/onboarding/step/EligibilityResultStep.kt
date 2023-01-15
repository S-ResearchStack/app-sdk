package healthstack.kit.task.onboarding.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.onboarding.model.EligibilityResultModel
import healthstack.kit.task.onboarding.view.EligibilityResultView

class EligibilityResultStep(
    id: String,
    name: String,
    model: EligibilityResultModel,
    view: View<EligibilityResultModel> = EligibilityResultView(),
) : Step<EligibilityResultModel, Boolean>(id, name, model, view, getResult = { true }) {

    val success: Boolean = true

    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
