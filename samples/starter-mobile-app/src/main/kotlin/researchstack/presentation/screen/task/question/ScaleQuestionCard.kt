package researchstack.presentation.screen.task.question

import androidx.compose.runtime.Composable
import researchstack.domain.model.task.question.ScaleQuestion
import researchstack.domain.model.task.question.common.Option

@Composable
fun ScaleQuestionCard(question: ScaleQuestion, onChangedResult: (String) -> Unit) {
    onChangedResult(question.low.toString())
    SliderGroup(
        options = listOf(
            Option(question.low.toString(), question.lowLabel),
            Option(question.high.toString(), question.highLabel)
        ),
        onChangedResult = onChangedResult
    )
}
