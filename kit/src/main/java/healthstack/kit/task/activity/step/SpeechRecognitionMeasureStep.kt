package healthstack.kit.task.activity.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.SpeechRecognitionMeasureModel
import healthstack.kit.task.activity.view.SpeechRecognitionMeasureView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View

class SpeechRecognitionMeasureStep(
    id: String,
    name: String,
    model: SpeechRecognitionMeasureModel,
    view: View<SpeechRecognitionMeasureModel> = SpeechRecognitionMeasureView(),
) : Step<SpeechRecognitionMeasureModel, Unit>(id, name, model, view, {}) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, null)
}
