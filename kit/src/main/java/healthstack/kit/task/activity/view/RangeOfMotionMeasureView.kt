package healthstack.kit.task.activity.view

import androidx.compose.runtime.Composable
import healthstack.kit.task.activity.model.RangeOfMotionMeasureModel
import healthstack.kit.task.activity.view.common.SimpleTimerActivityView
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.SubStepHolder

class RangeOfMotionMeasureView : SimpleTimerActivityView<RangeOfMotionMeasureModel>() {
    @Composable
    override fun Render(
        model: RangeOfMotionMeasureModel,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    ) {
        super.Render(model, callbackCollection, holder)
    }
}
