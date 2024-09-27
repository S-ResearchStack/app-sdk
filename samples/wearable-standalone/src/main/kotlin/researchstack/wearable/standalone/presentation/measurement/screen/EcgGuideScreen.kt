package researchstack.wearable.standalone.presentation.measurement.screen

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.presentation.component.GuideComponent
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.measurement.viewmodel.helper.getOrientation

@Composable
fun EcgGuideScreen(navController: NavHostController) {
    val drawable = when (getOrientation(LocalContext.current)) {
        Configuration.ORIENTATION_LANDSCAPE -> R.drawable.ecg_help_try_again_04_r
        else -> R.drawable.ecg_help_try_again_04_l
    }
    GuideComponent(drawable = drawable, HomeScreenItem.ECG.name, navController)
}
