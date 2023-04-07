package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.ColorWordChallengeMeasureModel
import healthstack.kit.task.activity.view.ColorWordChallengeMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class ColorWordChallengeMeasureStep(
    id: String,
    name: String,
    model: ColorWordChallengeMeasureModel,
    view: View<ColorWordChallengeMeasureModel> = ColorWordChallengeMeasureView(),
) : Step<ColorWordChallengeMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
