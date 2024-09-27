package researchstack.wearable.standalone.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import researchstack.wearable.standalone.presentation.main.screen.SettingScreen
import researchstack.wearable.standalone.presentation.measurement.Route
import researchstack.wearable.standalone.presentation.measurement.screen.AskProfile
import researchstack.wearable.standalone.presentation.measurement.screen.AskProfilePage
import researchstack.wearable.standalone.presentation.measurement.viewmodel.BiaMeasureViewModel
import researchstack.wearable.standalone.presentation.theme.HealthWearableTheme

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthWearableTheme {
                val startDest = Route.Main.name
                val navController = rememberNavController()
                val biaMeasureViewModel: BiaMeasureViewModel = hiltViewModel()
                NavHost(
                    navController = navController,
                    startDestination = startDest
                ) {
                    composable(Route.Main.name) {
                        SettingScreen(
                            biaMeasureViewModel = biaMeasureViewModel,
                            navController = navController
                        )
                    }

                    composable("${Route.AskProfile.name}/{item}") { navBackStackEntry ->
                        val page = AskProfilePage.valueOf(
                            navBackStackEntry.arguments?.getString("item")
                                ?: AskProfilePage.MEASUREMENT_UNIT.name
                        )
                        AskProfile(
                            biaMeasureViewModel = biaMeasureViewModel,
                            BiaMeasureViewModel.RequestProfile(
                                listOf(page)
                            )
                        ) {
                            navController.navigateUp()
                        }
                    }
                }
            }
        }
    }
}
