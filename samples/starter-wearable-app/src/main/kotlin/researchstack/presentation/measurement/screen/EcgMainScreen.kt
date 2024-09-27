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
import researchstack.presentation.measurement.viewmodel.EcgMainViewModel

@Composable
fun EcgMainScreen(
    navController: NavHostController,
    ecgMainViewModel: EcgMainViewModel = hiltViewModel()
) {
    val lastMeasurementTime = ecgMainViewModel.lastMeasurementTime.observeAsState().value
    MainScreenComponent(
        title = HomeScreenItem.ECG.getItemTitle(),
        icon = HomeScreenItem.ECG.getItemIcon(),
        content = R.string.ecg_message,
        lastMeasurementTime = lastMeasurementTime,
        onClickMeasure = { navController.navigate(Route.Guide.name) },
    )
}
