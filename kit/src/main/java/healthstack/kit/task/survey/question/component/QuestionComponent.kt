package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Choice
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.DateTime
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Image
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.MultipleChoice
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Ranking
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Text
import healthstack.kit.theme.AppTheme

abstract class QuestionComponent<T : QuestionModel<*>> : Component<T>() {
    @Composable
    override fun Render(model: T, callbackCollection: CallbackCollection) {
        Text(
            text = model.question,
            style = AppTheme.typography.title2,
            color = AppTheme.colors.onSurface,
        )

        if (!model.explanation.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = model.explanation,
                modifier = Modifier
                    .fillMaxWidth(),
                style = AppTheme.typography.body2,
                color = AppTheme.colors.onBackground.copy(0.6F)
            )
        }
    }

    companion object {
        fun defaultComponentOf(type: QuestionType): Component<out QuestionModel<*>> =
            when (type) {
                Choice -> ChoiceQuestionComponent()
                Text -> TextInputQuestionComponent()
                MultipleChoice -> MultiChoiceQuestionComponent()
                Ranking -> RankingQuestionComponent()
                DateTime -> DateTimeQuestionComponent()
                Image -> ImageQuestionComponent()
            }
    }
}
