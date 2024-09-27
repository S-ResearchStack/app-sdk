package researchstack.wearable.standalone.presentation.measurement

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import researchstack.data.repository.PrivDataRequester
import researchstack.wearable.standalone.presentation.main.MainActivity.Companion.PPG_BUNDLE_KEY
import researchstack.wearable.standalone.presentation.main.screen.HomeScreenItem
import researchstack.wearable.standalone.presentation.measurement.screen.PpgGuideScreen
import researchstack.wearable.standalone.presentation.measurement.screen.PpgMainScreen
import researchstack.wearable.standalone.presentation.measurement.screen.PpgMeasureScreen
import researchstack.wearable.standalone.presentation.service.WearableDataForegroundService
import researchstack.wearable.standalone.presentation.theme.HealthWearableTheme

@AndroidEntryPoint
class PpgActivity : ComponentActivity() {
    private lateinit var type: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runCatching {
            val extras = intent.extras ?: throw Exception("extras is Null")
            type = extras.getString(PPG_BUNDLE_KEY, HomeScreenItem.PPG_RED.name)
        }.onFailure {
            Log.e(PpgActivity::class.simpleName, "onCreate: ${it.message}")
            finish()
        }

        setContent {
            HealthWearableTheme {
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
                        composable(Route.Main.name) { PpgMainScreen(navController, type) }
                        composable(Route.Guide.name) { PpgGuideScreen(navController) }
                        composable(Route.Measure.name) { PpgMeasureScreen(navController, type) }
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
