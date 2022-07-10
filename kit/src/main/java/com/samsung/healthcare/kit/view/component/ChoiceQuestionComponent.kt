package com.samsung.healthcare.kit.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel
import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel.ViewType
import com.samsung.healthcare.kit.theme.AppTheme
import kotlin.math.roundToInt

class ChoiceQuestionComponent<T : ChoiceQuestionModel<*>> : QuestionComponent<T>() {

    private val modifier: Modifier = Modifier.fillMaxWidth(1f)

    @Composable
    override fun Render(model: T, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))

            when (model.viewType) {
                ViewType.Radio -> RadioGroup(model, modifier)
                ViewType.Slider -> SliderGroup(model, modifier)
                ViewType.DropMenu -> DropDownGroup(model, modifier)
            }
        }
    }

    @Composable
    private fun RadioGroup(choiceQuestion: ChoiceQuestionModel<*>, modifier: Modifier) {
        val rememberIndex = remember { mutableStateOf(choiceQuestion.selection) }
        rememberIndex.value = choiceQuestion.selection

        Column(modifier = modifier) {
            choiceQuestion.candidates.forEachIndexed { index, candidate ->
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    RadioButton(
                        selected = rememberIndex.value == index,
                        onClick = {
                            choiceQuestion.selection = index
                            rememberIndex.value = index
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
                if (index != choiceQuestion.candidates.lastIndex) {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    @Composable
    fun SliderGroup(question: ChoiceQuestionModel<*>, modifier: Modifier) {
        var sliderState by remember { mutableStateOf(question.selection ?: 0) }

        Column(modifier = modifier) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                question.candidates.forEach {
                    Text(
                        text = it.toString(),
                        style = AppTheme.typography.body1,
                        color = AppTheme.colors.textPrimary,
                    )
                }
            }
            Slider(
                value = sliderState.toFloat(),
                valueRange = 0f..(question.candidates.lastIndex).toFloat(),
                steps = question.candidates.size - 2,
                onValueChange = { newValue ->
                    sliderState = newValue.roundToInt()
                    question.selection = newValue.roundToInt()
                }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun DropDownGroup(
        question: ChoiceQuestionModel<*>,
        modifier: Modifier,
    ) {
        var selectedIndex by remember { mutableStateOf(question.selection) }
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = (selectedIndex?.let { question.candidates[it].toString() } ?: ""),
                onValueChange = { selected: String ->
                    selectedIndex = question.candidates
                        .indexOfFirst { it.toString() == selected }
                },
                textStyle = AppTheme.typography.body1.copy(color = AppTheme.colors.textPrimary),
                modifier = Modifier
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(25),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                // TODO handle placeholder
                placeholder = { Text(text = "Select one", color = AppTheme.colors.textHint) },
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = AppTheme.colors.background),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                question.candidates.forEachIndexed { index, candidate ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            selectedIndex = index
                            question.selection = index
                            expanded = false
                        }
                    ) {
                        RadioButton(
                            selected = selectedIndex == index,
                            onClick = null,
                            enabled = true,
                        )
                        Text(
                            text = candidate.toString(),
                            style = AppTheme.typography.body1,
                            color = AppTheme.colors.textPrimary,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
