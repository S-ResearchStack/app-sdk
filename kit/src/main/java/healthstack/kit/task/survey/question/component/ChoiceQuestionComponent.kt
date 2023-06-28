package healthstack.kit.task.survey.question.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import healthstack.kit.annotation.PreviewGenerated
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Dropdown
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Radio
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Slider
import healthstack.kit.theme.AppTheme
import kotlin.math.roundToInt

class ChoiceQuestionComponent<T : ChoiceQuestionModel<*>> : QuestionComponent<T>() {

    private val modifier: Modifier = Modifier.fillMaxWidth()

    @Composable
    override fun Render(model: T, callbackCollection: CallbackCollection) {
        Column {
            super.Render(model, callbackCollection)

            Spacer(modifier = Modifier.height(20.dp))

            when (model.viewType) {
                Radio -> RadioGroup(model, modifier)
                Slider -> SliderGroup(model)
                Dropdown -> DropDownGroup(model)
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
    ) {
        var selectedIndex by remember { mutableStateOf(question.selection) }
        var expanded by remember { mutableStateOf(true) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = (selectedIndex?.let { question.candidates[it].toString() } ?: ""),
                onValueChange = { selected: String ->
                    selectedIndex = question.candidates
                        .indexOfFirst { it == selected }
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
                question.candidates.forEachIndexed { index, candidate ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            selectedIndex = index
                            question.selection = index
                            expanded = false
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
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
}

@Composable
fun RadioGroup(choiceQuestion: ChoiceQuestionModel<*>, modifier: Modifier) {
    val rememberIndex = remember { mutableStateOf(choiceQuestion.selection) }
    rememberIndex.value = choiceQuestion.selection

    Column(modifier = modifier) {
        choiceQuestion.candidates.forEachIndexed { index, candidate ->
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
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
        viewType = Dropdown
    )

    component.Render(model, CallbackCollection())
}
