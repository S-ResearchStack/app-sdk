@file:Suppress("MagicNumber")

package researchstack.presentation.screen.task.question

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import researchstack.domain.model.task.question.ChoiceQuestion
import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionTag.CHECKBOX
import researchstack.domain.model.task.question.common.QuestionTag.DROPDOWN
import researchstack.domain.model.task.question.common.QuestionTag.RADIO
import researchstack.domain.model.task.question.common.QuestionTag.SLIDER
import researchstack.presentation.component.LabeledCheckbox
import researchstack.presentation.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun ChoiceQuestionCard(question: ChoiceQuestion, onChangedResult: (String) -> Unit) {
    when (question.tag) {
        SLIDER -> {
            onChangedResult(question.options.first().value)
            SliderGroup(question.options, onChangedResult)
        }

        RADIO -> RadioGroup(question, onChangedResult)
        DROPDOWN -> DropDownGroup(question, onChangedResult)
        CHECKBOX -> TODO()
        else -> {
            Log.e("ChoiceQuestionCard", "not supported choice question tag: ${question.tag}")
            RadioGroup(question, onChangedResult)
        }
    }
}

@Composable
fun RadioGroup(choiceQuestion: ChoiceQuestion, onChangedResult: (String) -> Unit) {
    var selection by remember { mutableStateOf<Int?>(null) }

    Column {
        choiceQuestion.options.forEachIndexed { index, (value) ->
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .height(40.dp)
                    .clickable {
                        selection = index
                        onChangedResult(choiceQuestion.options[index].value)
                    }
            ) {
                RadioButton(
                    selected = selection == index,
                    onClick = null,
                    enabled = true,
                    modifier = Modifier
                        .testTag(value)
                        .size(24.dp),
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.primary,
                        unselectedColor = AppTheme.colors.primary.copy(0.3F),
                        disabledColor = AppTheme.colors.disabled,
                    )
                )
                Text(
                    text = value,
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun SliderGroup(options: List<Option>, onChangedResult: (String) -> Unit) {
    val low: Float = (options.first().value.toFloat())
    val high: Float = (options.last().value.toFloat())
    var sliderState by remember { mutableStateOf(low.toInt()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 20.dp)
    ) {
        Slider(
            value = sliderState.toFloat(),
            valueRange = low..high,
            steps = (high - low - 1).toInt(),
            onValueChange = { newValue ->
                sliderState = newValue.roundToInt()
                onChangedResult(sliderState.toString())
            },
            colors = SliderDefaults.colors(
                thumbColor = AppTheme.colors.primary,
                activeTickColor = AppTheme.colors.primary,
                activeTrackColor = AppTheme.colors.primary
            )
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach {
                Text(
                    text = it.value,
                    style = AppTheme.typography.overline1,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownGroup(choiceQuestion: ChoiceQuestion, onChangedResult: (String) -> Unit) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var expanded by remember { mutableStateOf(true) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = (selectedIndex?.let { choiceQuestion.options[it].value } ?: ""),
            onValueChange = { selected: String ->
                selectedIndex = choiceQuestion.options
                    .indexOfFirst { it.value == selected }
                onChangedResult(selected)
            },
            textStyle = AppTheme.typography.body1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = AppTheme.colors.onSurface,
                disabledTextColor = AppTheme.colors.onSurface.copy(0.6F),
                focusedBorderColor = AppTheme.colors.primary,
                unfocusedBorderColor = AppTheme.colors.primary.copy(0.38F),
                trailingIconColor = AppTheme.colors.primary
            ),
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(4.dp),
            placeholder = { Text(text = "Select One", color = AppTheme.colors.onSurface.copy(0.6F)) },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .exposedDropdownSize()
        ) {
            choiceQuestion.options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        selectedIndex = index
                        expanded = false
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    DropdownMenuRow(selectedIndex, index, option)
                }
            }
        }
    }
}

@Composable
private fun DropdownMenuRow(
    selectedIndex: Int?,
    index: Int,
    option: Option,
) {
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(54.dp)
            .let {
                if (selectedIndex == index) {
                    return@let it.background(
                        AppTheme.colors.primary.copy(0.08F)
                    )
                }
                it
            }
    ) {
        RadioButton(
            selected = selectedIndex == index,
            onClick = null,
            enabled = true,
            colors = RadioButtonDefaults.colors(
                selectedColor = AppTheme.colors.primary,
                unselectedColor = AppTheme.colors.primary.copy(0.3F),
                disabledColor = AppTheme.colors.disabled,
            ),
            modifier = Modifier.padding(start = 20.dp)
        )
        Text(
            text = option.toString(),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        )
    }
}

@Composable
fun CheckboxGroup(choiceQuestion: ChoiceQuestion, onChangedResult: (String) -> Unit) {
    val selectedIndexes = mutableSetOf<Int>()

    Column {
        choiceQuestion.options.forEachIndexed { index, option ->
            Row(
                verticalAlignment = CenterVertically
            ) {
                var checkedState by remember { mutableStateOf(false) }
                LabeledCheckbox(
                    isChecked = checkedState,
                    onCheckedChange = { checked ->
                        checkedState = checked
                        if (checked) {
                            selectedIndexes.add(index)
                        } else {
                            selectedIndexes.remove(index)
                        }

                        onChangedResult(
                            selectedIndexes.sorted()
                                .map { choiceQuestion.options[it].value }
                                .joinToString(",")
                        )
                    },
                    labelText = option.value
                )
            }
        }
    }
}
