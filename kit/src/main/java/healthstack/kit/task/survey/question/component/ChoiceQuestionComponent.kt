package healthstack.kit.task.survey.question.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.DropMenu
import healthstack.kit.theme.AppTheme
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
                ViewType.Slider -> SliderGroup(model)
                ViewType.DropMenu -> DropDownGroup(model, modifier)
            }
        }
    }

    @Composable
    fun SliderGroup(question: ChoiceQuestionModel<*>) {
        var sliderState by remember { mutableStateOf(question.selection ?: 0) }

        val low: Float = (question.candidates.first() as Int).toFloat()
        val high: Float = (question.candidates.last() as Int).toFloat()
        val columnModifier = modifier
            .fillMaxWidth()
            .padding(vertical = 50.dp, horizontal = 20.dp)

        Column(
            modifier = columnModifier
        ) {
            Slider(
                value = sliderState.toFloat(),
                valueRange = low..high,
                steps = (high - low - 1).toInt(),
                onValueChange = { newValue ->
                    sliderState = newValue.roundToInt()
                    question.selection = newValue.roundToInt()
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
                question.candidates.forEach {
                    Text(
                        text = it.toString(),
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
    fun DropDownGroup(
        question: ChoiceQuestionModel<*>,
        modifier: Modifier,
    ) {
        var selectedIndex by remember { mutableStateOf(question.selection) }
        var expanded by remember { mutableStateOf(true) }

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
                textStyle = AppTheme.typography.body1,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = AppTheme.colors.onSurface,
                    disabledTextColor = AppTheme.colors.onSurface.copy(0.6F),
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.primary.copy(0.38F),
                    trailingIconColor = AppTheme.colors.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(4.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                placeholder = { Text(text = "Select One", color = AppTheme.colors.onSurface.copy(0.6F)) },
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
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
                            colors = RadioButtonDefaults.colors(
                                selectedColor = AppTheme.colors.primary,
                                unselectedColor = AppTheme.colors.primary.copy(0.3F),
                                disabledColor = AppTheme.colors.disabled,
                            )
                        )

                        Text(
                            text = candidate.toString(),
                            style = AppTheme.typography.body2,
                            color = AppTheme.colors.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RadioGroup(choiceQuestion: ChoiceQuestionModel<*>, modifier: Modifier) {
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
                    colors = RadioButtonDefaults.colors(
                        selectedColor = AppTheme.colors.primary,
                        unselectedColor = AppTheme.colors.primary.copy(0.3F),
                        disabledColor = AppTheme.colors.disabled,
                    )
                )
                Text(
                    text = candidate.toString(),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            if (index != choiceQuestion.candidates.lastIndex) {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun RadioPreview() {
    val component = ChoiceQuestionComponent<ChoiceQuestionModel<String>>()
    val model = ChoiceQuestionModel<String>(
        id = "radio",
        query = "Are you designer?",
        candidates = listOf("developer", "designer")
    )

    component.Render(model, CallbackCollection())
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun SliderPreview() {
    val component = ChoiceQuestionComponent<ChoiceQuestionModel<Int>>()
    val model = ChoiceQuestionModel<Int>(
        id = "slider",
        query = "How was your symptom level for headaches?",
        explanation = "Please tap on the slider to give a rating, from 0 " +
            "being no concern to 10 being extremely concerned.",
        candidates = listOf(0, 10),
        viewType = ViewType.Slider
    )

    component.Render(model, CallbackCollection())
}

@PreviewGenerated
@Preview(showBackground = true)
@Composable
fun DropdownPreview() {
    val component = ChoiceQuestionComponent<ChoiceQuestionModel<Int>>()
    val model = ChoiceQuestionModel<Int>(
        id = "slider",
        query = "How was your symptom level for headaches?",
        explanation = "Please tap on the slider to give a rating, from 0 " +
            "being no concern to 10 being extremely concerned.",
        candidates = (20..50).toList(),
        viewType = DropMenu
    )

    component.Render(model, CallbackCollection())
}
