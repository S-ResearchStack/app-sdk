package researchstack.presentation.measurement.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import researchstack.R
import researchstack.presentation.component.MainScreenComponent
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.main.screen.getItemIcon
import researchstack.presentation.main.screen.getItemTitle
import researchstack.presentation.measurement.Route
import researchstack.presentation.measurement.viewmodel.SpO2MainViewModel

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
