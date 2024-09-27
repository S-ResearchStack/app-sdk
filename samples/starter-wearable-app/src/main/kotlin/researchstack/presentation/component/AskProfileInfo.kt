package researchstack.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.RadioButton
import androidx.wear.compose.material.RadioButtonDefaults
import androidx.wear.compose.material.Text
import researchstack.R
import researchstack.domain.model.Gender
import researchstack.presentation.theme.Blue100
import researchstack.presentation.theme.ItemHomeColor
import researchstack.presentation.theme.TextGray
import researchstack.presentation.theme.Typography
import java.util.Calendar

@Composable
fun AskGender(defaultValue: Gender, buttonLabel: Int, action: (Gender) -> Unit) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedRingColor = Blue100,
        selectedDotColor = Blue100,
        unselectedRingColor = Color.LightGray,
        unselectedDotColor = ItemHomeColor
    )
    var gender by remember { mutableStateOf(defaultValue) }
    val listGender = listOf(Gender.FEMALE, Gender.MALE)
    val listGenderString = listOf(R.string.bia_female, R.string.bia_male)
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.bia_select_gender),
            color = Color.White,
            style = Typography.body1
        )
        Column(
            Modifier
                .background(color = ItemHomeColor, shape = RoundedCornerShape(24.dp))
                .fillMaxWidth(0.9f)
        ) {
            for (i in listGender.indices) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                        .clickable { gender = listGender[i] }
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = gender == listGender[i],
                        Modifier.clickable {
                            gender = listGender[i]
                        },
                        colors = radioButtonColors
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(id = listGenderString[i]),
                        Modifier.clickable {
                            gender = listGender[i]
                        }
                    )
                }
                if (i == 0) {
                    Divider(Modifier.padding(8.dp, 0.dp), color = Color.LightGray)
                }
            }
        }
        AppButton(bgColor = Blue100, title = stringResource(id = buttonLabel)) {
            action(gender)
        }
    }
}

@Composable
fun AskMeasurementUnit(
    defaultValue: Boolean,
    buttonLabel: Int,
    action: (Boolean) -> Unit
) {
    val radioButtonColors = RadioButtonDefaults.colors(
        selectedRingColor = Blue100,
        selectedDotColor = Blue100,
        unselectedRingColor = Color.LightGray,
        unselectedDotColor = ItemHomeColor
    )
    var isMetric by remember { mutableStateOf(defaultValue) }
    val listValue = listOf(false, true)
    val listLabel = listOf(
        "${stringResource(id = R.string.bia_imperial_ft_unit)}/${stringResource(id = R.string.bia_imperial_weight_unit)}",
        "${stringResource(id = R.string.bia_metric_height_unit)}/${stringResource(id = R.string.bia_metric_weight_unit)}"
    )
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (buttonLabel == R.string.next) Arrangement.Center else Arrangement.SpaceAround,
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.bia_select_unit),
            color = Color.White,
            style = Typography.body1,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Column(
            Modifier
                .background(color = ItemHomeColor, shape = RoundedCornerShape(24.dp))
                .fillMaxWidth(0.9f)
        ) {
            for (i in listValue.indices) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                        .clickable { isMetric = listValue[i] }
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(
                        selected = isMetric == listValue[i],
                        Modifier.clickable {
                            isMetric = listValue[i]
                        },
                        colors = radioButtonColors
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = listLabel[i],
                        Modifier.clickable {
                            isMetric = listValue[i]
                        }
                    )
                }
                if (i == 0) {
                    Divider(Modifier.padding(8.dp, 0.dp), color = Color.LightGray)
                }
            }
        }
        if (buttonLabel == R.string.next) {
            Text(
                text = stringResource(id = R.string.bia_help_change_unit),
                color = TextGray,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                textAlign = TextAlign.Center
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }
        AppButton(
            bgColor = Blue100,
            title = stringResource(id = buttonLabel)
        ) {
            action(isMetric)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AskFloat(
    title: String,
    unit: FloatPickerLabel,
    range: IntRange,
    defaultValue: Float = 170f,
    buttonLabel: Int,
    action: (Float) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = Color.White,
            style = Typography.body1,
            textAlign = TextAlign.Center
        )
        Column {
            FloatPicker(
                range,
                label = unit,
                defaultValue = defaultValue,
                buttonLabel = buttonLabel
            ) {
                action(it)
            }
        }
    }
}

@Composable
fun AskYearBirth(defaultValue: Int = 2001, buttonLabel: Int, action: (Int) -> Unit) {
    val year = Calendar.getInstance().get(Calendar.YEAR)
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.bia_set_age),
            color = Color.White,
            style = Typography.body1,
            textAlign = TextAlign.Center,
        )

        Column {
            IntPicker(
                year - 100..year,
                "",
                defaultValue = defaultValue,
                buttonLabel = buttonLabel
            ) {
                action(it)
            }
        }
    }
}
