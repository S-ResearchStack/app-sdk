package researchstack.wearable.standalone.presentation.measurement.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.GuideComponent
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem

@Composable
fun PpgGuideScreen(navController: NavHostController) {
    GuideComponent(R.drawable.help_try_again_01_l, HomeScreenItem.PPG_IR.name, navController)
}
