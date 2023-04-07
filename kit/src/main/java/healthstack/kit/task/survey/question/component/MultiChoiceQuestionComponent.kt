package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel
import healthstack.kit.ui.LabeledCheckbox

class MultiChoiceQuestionComponent<T : MultiChoiceQuestionModel> : QuestionComponent<T>() {

    private val modifier: Modifier = Modifier.fillMaxWidth()

    @Composable
    override fun Render(model: T, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))
            CheckboxGroup(model, modifier)
        }
    }

    @Composable
    private fun CheckboxGroup(multiChoiceQuestionModel: MultiChoiceQuestionModel, modifier: Modifier) {
        Column(modifier = modifier) {
            multiChoiceQuestionModel.candidates.forEachIndexed { index, candidate ->
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    val checkedState = remember(multiChoiceQuestionModel.id + index) {
                        mutableStateOf(multiChoiceQuestionModel.isSelected(index))
                    }
                    LabeledCheckbox(
                        isChecked = checkedState.value,
                        onCheckedChange = { checked ->
                            checkedState.value = checked
                            if (checked) multiChoiceQuestionModel.select(index)
                        },
                        labelText = candidate
                    )
                }
                if (index != multiChoiceQuestionModel.candidates.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    @PreviewGenerated
    @Preview(showBackground = true)
    @Composable
    fun CheckBoxGroupPreview() =
        CheckboxGroup(
            MultiChoiceQuestionModel(
                "id",
                "Preview Question?",
                "Explanation of the question",
                candidates = listOf(
                    "This is the sample answer 1",
                    "This is the sample answer 2",
                    "This is the sample answer 3 This is the sample answer 3 "
                )
            ),
            modifier
        )
}
