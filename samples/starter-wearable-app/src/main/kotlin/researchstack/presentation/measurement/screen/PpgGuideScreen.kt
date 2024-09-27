package researchstack.presentation.measurement.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import researchstack.R
import researchstack.presentation.component.GuideComponent
import researchstack.presentation.main.screen.HomeScreenItem

@Composable
fun PpgGuideScreen(navController: NavHostController) {
    GuideComponent(R.drawable.help_try_again_01_l, HomeScreenItem.PPG_IR.name, navController)
}
