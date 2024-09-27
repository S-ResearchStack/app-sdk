package researchstack.presentation.measurement.screen

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import researchstack.R
import researchstack.presentation.component.GuideComponent
import researchstack.presentation.main.screen.HomeScreenItem
import researchstack.presentation.measurement.viewmodel.helper.getOrientation

@Composable
fun EcgGuideScreen(navController: NavHostController) {
    val drawable = when (getOrientation(LocalContext.current)) {
        Configuration.ORIENTATION_LANDSCAPE -> R.drawable.ecg_help_try_again_04_r
        else -> R.drawable.ecg_help_try_again_04_l
    }
    GuideComponent(drawable = drawable, HomeScreenItem.ECG.name, navController)
}
