package researchstack.wearable.standalone.presentation.main.screen

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Text
import researchstack.domain.model.Gender
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.screen.AskProfilePage
import researchstack.wearable.standalone.presentation.measurement.screen.toInt
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.cmToFt
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.kgToLbs
import researchstack.wearable.standalone.presentation.theme.Blue100
import researchstack.wearable.standalone.presentation.theme.ItemHomeColor
import researchstack.wearable.standalone.presentation.theme.TextGray
import researchstack.wearable.standalone.presentation.theme.Typography

@Composable
fun SettingScreen(
    biaMeasureViewModel: BiaMeasureViewModel,
    navController: NavHostController,
) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        biaMeasureViewModel.isMetric.observeAsState().value?.let { isMetric ->
            var isMetric_ by remember { mutableStateOf(-1) }
            var height by remember { mutableStateOf(-1f) }
            var weight by remember { mutableStateOf(-1f) }
            var yearBirth by remember { mutableStateOf(-1) }
            var gender by remember { mutableStateOf(Gender.UNKNOWN) }

            biaMeasureViewModel.profile?.let {
                gender = it.gender
                height = it.height.cmToFt(isMetric)
                weight = it.weight.kgToLbs(isMetric)
                yearBirth = it.yearBirth
                isMetric_ = it.isMetricUnit.toInt()
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.settings),
                style = Typography.body1
            )
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = ItemHomeColor, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
            ) {
                SettingItem(
                    title = stringResource(id = R.string.bia_measurement_unit),
                    value = isMetric_.toUnitString()
                ) {
                    navController.navigate("${Route.AskProfile.name}/${AskProfilePage.MEASUREMENT_UNIT}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(id = R.string.bia_your_profile), color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(color = ItemHomeColor, shape = RoundedCornerShape(15))
                    .clip(RoundedCornerShape(20))
            ) {
                SettingItem(
                    title = stringResource(id = R.string.bia_gender_title),
                    value = gender.toGenderString(),
                ) {
                    navController.navigate("${Route.AskProfile.name}/${AskProfilePage.GENDER}")
                }

                SettingItem(
                    title = stringResource(id = R.string.bia_age_title),
                    value = yearBirth.toYearString(),
                ) {
                    navController.navigate("${Route.AskProfile.name}/${AskProfilePage.AGE}")
                }

                SettingItem(
                    title = stringResource(id = R.string.bia_height_title),
                    value = height.toHeightString(isMetric = isMetric),
                ) {
                    navController.navigate("${Route.AskProfile.name}/${AskProfilePage.HEIGHT}")
                }

                SettingItem(
                    title = stringResource(id = R.string.bia_weight_title),
                    value = weight.toWeightString(isMetric = isMetric),
                ) {
                    navController.navigate("${Route.AskProfile.name}/${AskProfilePage.WEIGHT}")
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SettingItem(title: String, value: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
            .clickable {
                onClick()
            }
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = title)
            if (value.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = value, color = Blue100)
            }
        }
    }
}

@Composable
fun Int.toUnitString(): String = when (this) {
    1 -> {
        val cm = stringResource(id = R.string.bia_metric_height_unit)
        val kg = stringResource(id = R.string.bia_metric_weight_unit)
        "$cm/$kg"
    }

    0 -> {
        val ft = stringResource(id = R.string.bia_imperial_ft_unit)
        val inch = stringResource(id = R.string.bia_imperial_weight_unit)
        "$ft/$inch"
    }

    else -> ""
}

fun Int.toYearString() = if (this < 0) "" else this.toString()

@Composable
private fun Gender.toGenderString(): String = when (this) {
    Gender.FEMALE -> stringResource(id = R.string.bia_female)
    Gender.MALE -> stringResource(id = R.string.bia_male)
    else -> ""
}

@Composable
private fun Float.toHeightString(isMetric: Boolean): String =
    if (this < 0f) "" else when (isMetric) {
        true -> "%.1f ${stringResource(id = R.string.bia_metric_height_unit)}".format(this)
        else -> {
            val before = this.toInt()
            val after = (this * 10).toInt() % 10
            val ft = stringResource(id = R.string.bia_imperial_ft_unit)
            val inch = stringResource(id = R.string.bia_imperial_in_unit)
            "$before $ft $after $inch"
        }
    }

@Composable
private fun Float.toWeightString(isMetric: Boolean): String =
    if (this < 0f) "" else when (isMetric) {
        true -> "%.1f ${stringResource(id = R.string.bia_metric_weight_unit)}".format(this)
        else -> "%.1f ${stringResource(id = R.string.bia_imperial_weight_unit)}".format(this)
    }
