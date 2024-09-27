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
import researchstack.presentation.measurement.viewmodel.BiaMainViewModel

@Composable
fun BiaMainScreen(
    navController: NavHostController,
    biaMainViewModel: BiaMainViewModel = hiltViewModel(),
) {
    val lastMeasurementTime = biaMainViewModel.lastMeasurementTime.observeAsState().value
    MainScreenComponent(
        title = HomeScreenItem.BODY_COMPOSITION.getItemTitle(),
        icon = HomeScreenItem.BODY_COMPOSITION.getItemIcon(),
        content = R.string.bia_message,
        lastMeasurementTime = lastMeasurementTime,
        onClickMeasure = { navController.navigate(Route.Guide.name) },
    )
}
