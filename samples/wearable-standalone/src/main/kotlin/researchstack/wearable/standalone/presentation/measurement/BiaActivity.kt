package researchstack.wearable.standalone.presentation.measurement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import researchstack.data.repository.PrivDataRequester
import researchstack.wearable.standalone.presentation.measurement.screen.BiaGuideScreen
import researchstack.wearable.standalone.presentation.measurement.screen.BiaMainScreen
import researchstack.wearable.standalone.presentation.measurement.screen.BiaMeasureScreen
import researchstack.wearable.standalone.presentation.service.WearableDataForegroundService

@AndroidEntryPoint
class BiaActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                var isReady by remember { mutableStateOf(PrivDataRequester.isConnected) }

                if (!isReady) {
                    PrivDataRequester.initialize(
                        this,
                        applicationContext,
                        onConnectionSuccess = { isReady = true }
                    )
                    PrivDataRequester.healthTrackingService.connectService()
                } else {
                    val startDest = Route.Main.name
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = startDest
                    ) {
                        composable(Route.Main.name) { BiaMainScreen(navController) }
                        composable(Route.Guide.name) { BiaGuideScreen(navController) }
                        composable(Route.Measure.name) { BiaMeasureScreen(navController) }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (WearableDataForegroundService.isRunning.not()) PrivDataRequester.healthTrackingService.disconnectService()
    }
}
