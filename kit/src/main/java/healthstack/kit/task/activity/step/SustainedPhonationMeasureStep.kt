package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.SustainedPhonationMeasureModel
import healthstack.kit.task.activity.view.SustainedPhonationMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class SustainedPhonationMeasureStep(
    id: String,
    name: String,
    model: SustainedPhonationMeasureModel,
    view: View<SustainedPhonationMeasureModel> = SustainedPhonationMeasureView(),
) : Step<SustainedPhonationMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
