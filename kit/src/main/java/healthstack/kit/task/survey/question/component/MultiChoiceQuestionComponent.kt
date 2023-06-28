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
import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel.ViewType.Checkbox
import healthstack.kit.ui.LabeledCheckbox

class MultiChoiceQuestionComponent : QuestionComponent<MultiChoiceQuestionModel>() {

    private val modifier: Modifier = Modifier.fillMaxWidth()

    @Composable
    override fun Render(model: MultiChoiceQuestionModel, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))

            when (model.viewType) {
                Checkbox -> CheckboxGroup(model, modifier)
            }
        }
    }

    @Composable
    private fun CheckboxGroup(model: MultiChoiceQuestionModel, modifier: Modifier) =
        Column(modifier = modifier) {
            model.candidates.forEachIndexed { index, candidate ->
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    val checkedState = remember(model.id + index) {
                        mutableStateOf(model.isSelected(index))
                    }
                    LabeledCheckbox(
                        isChecked = checkedState.value,
                        onCheckedChange = { checked ->
                            checkedState.value = checked
                            if (checked) model.select(index)
                            else model.deselect(index)
                        },
                        labelText = candidate
                    )
                }
            }
        }

    @PreviewGenerated
    @Preview(showBackground = true)
    @Composable
    private fun CheckBoxGroupPreview() =
        CheckboxGroup(
            MultiChoiceQuestionModel(
                "id",
                "Preview Question?",
                "Explanation of the question",
                candidates = listOf(
                    "This is the sample answer 1",
                    "This is the sample answer 2",
                    "This is the sample answer 3 This is the sample answer 3 "
                ),
                tag = "checkbox"
            ),
            modifier
        )
}
