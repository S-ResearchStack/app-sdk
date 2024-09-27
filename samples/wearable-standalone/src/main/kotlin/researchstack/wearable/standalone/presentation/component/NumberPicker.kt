package researchstack.wearable.standalone.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.PickerDefaults
import androidx.wear.compose.material.ProvideTextStyle
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberPickerState
import researchstack.wearable.standalone.presentation.theme.Blue100
import researchstack.wearable.standalone.presentation.theme.Typography
import researchstack.wearable.standalone.presentation.theme.UnitColor

sealed class FloatPickerLabel
class OneLabel(val label: String) : FloatPickerLabel()
class TwoLabel(
    val firstLabel: String,
    val secondLabel: String
) : FloatPickerLabel()

@Composable
fun FloatPicker(
    range: IntRange = 100..300,
    label: FloatPickerLabel,
    buttonLabel: Int,
    defaultValue: Float = 24f,
    onSubmit: (Float) -> Unit = {},
) {
    val itemsBeforeDot = ArrayList<Int>()
    for (i in range) {
        itemsBeforeDot.add(i)
    }
    val itemsAfterDot = ArrayList<Int>()
    for (i in 0..9) {
        itemsAfterDot.add(i)
    }
    val stateBeforeDot = rememberPickerState(itemsBeforeDot.size)
    val stateAfterDot = rememberPickerState(itemsAfterDot.size)
    val contentBeforeDot by remember { derivedStateOf { "${stateBeforeDot.selectedOption + 1}" } }
    val contentAfterDot by remember { derivedStateOf { "${stateAfterDot.selectedOption + 1}" } }
    var isSmalerThan10 by remember { mutableStateOf(defaultValue < 10) }

    val num1 = defaultValue.toInt()
    val num2 = defaultValue.times(10).toInt() % 10

    itemsBeforeDot.indexOf(num1).let {
        if (it != -1) {
            LaunchedEffect(null) {
                stateBeforeDot.scrollToOption(it)
            }
        }
    }
    itemsAfterDot.indexOf(num2).let {
        if (it != -1) {
            LaunchedEffect(null) {
                stateAfterDot.scrollToOption(it)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            ProvideTextStyle(value = Typography.title2) {
                var isBeforeSelected by remember {
                    mutableStateOf(true)
                }
                var isAfterSelected by remember {
                    mutableStateOf(false)
                }

                Picker(
                    modifier = Modifier
                        .size(if (isSmalerThan10) 48.dp else 96.dp, 100.dp)
                        .pointerInput(null) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                    isSmalerThan10 =
                                        itemsBeforeDot[stateBeforeDot.selectedOption] < 10
                                    isBeforeSelected = true
                                    isAfterSelected = false
                                }
                            }
                        },
                    state = stateBeforeDot,
                    contentDescription = contentBeforeDot,
                    scalingParams = PickerDefaults.defaultScalingParams(
                        edgeScale = 0.02f,
                        edgeAlpha = 0.3f,
                    ),
                    readOnly = !isBeforeSelected,
                    gradientRatio = 0.0f,
                ) {
                    Text(
                        itemsBeforeDot[it].toString(),
                        color = if (isBeforeSelected) Blue100 else Color.White
                    )
                }
                Text(text = ".", Modifier.padding(bottom = 20.dp))
                Picker(
                    modifier = Modifier
                        .size(50.dp, 100.dp)
                        .pointerInput(null) {
                            awaitPointerEventScope {
                                while (true) {
                                    awaitPointerEvent()
                                    isBeforeSelected = false
                                    isAfterSelected = true
                                }
                            }
                        },
                    state = stateAfterDot,
                    contentDescription = contentAfterDot,
                    scalingParams = PickerDefaults.defaultScalingParams(
                        edgeScale = 0.02f,
                        edgeAlpha = 0.3f,
                    ),
                    readOnly = !isAfterSelected,
                    gradientRatio = 0.0f,
                ) {
                    Text(
                        itemsAfterDot[it].toString(),
                        color = if (isAfterSelected) Blue100 else Color.White
                    )
                }
            }
        }
        if (label is OneLabel) {
            Text(
                text = label.label,
                color = UnitColor,
                style = Typography.body1
            )
        } else if (label is TwoLabel) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(
                    text = label.firstLabel,
                    color = UnitColor,
                    style = Typography.body1
                )
                Spacer(modifier = Modifier.size(32.dp))
                Text(
                    text = label.secondLabel,
                    color = UnitColor,
                    style = Typography.body1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        AppButton(bgColor = Blue100, title = stringResource(id = buttonLabel)) {
            onSubmit(
                calcFloat(
                    itemsBeforeDot[stateBeforeDot.selectedOption],
                    itemsAfterDot[stateAfterDot.selectedOption],
                )
            )
        }
    }
}

@Composable
fun IntPicker(
    range: IntRange = 100..300,
    label: String,
    buttonLabel: Int,
    defaultValue: Int = 24,
    onSubmit: (Int) -> Unit = {},
) {
    val items = ArrayList<Int>()
    for (i in range) {
        items.add(i)
    }

    val state = rememberPickerState(items.size)
    val content by remember { derivedStateOf { "${state.selectedOption + 1}" } }
    items.indexOf(defaultValue).let {
        if (it != -1) {
            LaunchedEffect(null) {
                state.scrollToOption(it)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            ProvideTextStyle(value = Typography.title2) {
                Picker(
                    modifier = Modifier
                        .size(150.dp, 100.dp),
                    state = state,
                    contentDescription = content,
                    gradientRatio = 0.0f,
                    scalingParams = PickerDefaults.defaultScalingParams(
                        edgeScale = 0.01f,
                        edgeAlpha = 0.3f,
                    ),
                ) {
                    Text(
                        items[it].toString(),
                        color = Blue100
                    )
                }
            }
        }
        Text(
            text = label,
            color = UnitColor,
            style = Typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(bgColor = Blue100, title = stringResource(id = buttonLabel)) {
            onSubmit(items[state.selectedOption])
        }
    }
}

private fun calcFloat(before: Int, after: Int): Float =
    before.toFloat() + after.toFloat() / 10f
