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
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMainViewModel

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
