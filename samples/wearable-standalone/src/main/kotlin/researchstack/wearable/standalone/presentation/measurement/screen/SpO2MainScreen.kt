package researchstack.wearable.standalone.presentation.measurement.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.MainScreenComponent
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.main.screen.getItemIcon
import researchstack.wearable.standalone.presentation.main.screen.getItemTitle
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.viewmodel.SpO2MainViewModel

@Composable
fun SpO2MainScreen(
    navController: NavHostController,
    spO2MainViewModel: SpO2MainViewModel = hiltViewModel(),
) {
    val lastMeasurementTime = spO2MainViewModel.lastMeasurementTime.observeAsState().value

    MainScreenComponent(
        title = HomeScreenItem.BLOOD_OXYGEN.getItemTitle(),
        icon = HomeScreenItem.BLOOD_OXYGEN.getItemIcon(),
        content = R.string.blood_oxygen_message,
        lastMeasurementTime = lastMeasurementTime,
        onClickMeasure = { navController.navigate(Route.Guide.name) },
    )
}
