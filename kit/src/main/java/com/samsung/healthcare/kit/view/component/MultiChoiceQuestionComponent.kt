package com.samsung.healthcare.kit.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.question.MultiChoiceQuestionModel
import com.samsung.healthcare.kit.theme.AppTheme

class MultiChoiceQuestionComponent<T : MultiChoiceQuestionModel> : QuestionComponent<T>() {

    private val modifier: Modifier = Modifier.fillMaxWidth(1f)

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
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = { checked ->
                            checkedState.value = checked
                            if (checked) multiChoiceQuestionModel.select(index)
                        },
                        enabled = true,
                        modifier = Modifier.testTag(candidate.toString()),
                    )
                    Text(
                        text = candidate.toString(),
                        style = AppTheme.typography.body1,
                        color = AppTheme.colors.textPrimary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                if (index != multiChoiceQuestionModel.candidates.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
