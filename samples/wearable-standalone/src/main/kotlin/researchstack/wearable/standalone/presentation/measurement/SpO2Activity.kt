package researchstack.wearable.standalone.presentation.measurement

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.wear.compose.material.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint
import researchstack.data.repository.PrivDataRequester
import researchstack.wearable.standalone.presentation.PermissionChecker
import researchstack.wearable.standalone.presentation.measurement.screen.SpO2GuideScreen
import researchstack.wearable.standalone.presentation.measurement.screen.SpO2MainScreen
import researchstack.wearable.standalone.presentation.measurement.screen.SpO2MeasureScreen
import researchstack.wearable.standalone.presentation.service.WearableDataForegroundService

@AndroidEntryPoint
class SpO2Activity : ComponentActivity() {
    private val requiredPermissions = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var isReady by remember { mutableStateOf(PrivDataRequester.isConnected) }

                if (!isReady) {
                    PrivDataRequester.initialize(this, applicationContext, onConnectionSuccess = { isReady = true })
                    PrivDataRequester.healthTrackingService.connectService()
                } else {
                    var isGranted by remember { mutableStateOf(isPermissionsGranted()) }
                    if (!isGranted) {
                        PermissionChecker(requiredPermissions) {
                            Log.i("PermissionChecker", "callback PermissionChecker")
                            isGranted = isPermissionsGranted()
                        }
                    } else {
                        val startDest = Route.Main.name
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = startDest
                        ) {
                            composable(Route.Main.name) { SpO2MainScreen(navController) }
                            composable(Route.Guide.name) { SpO2GuideScreen(navController) }
                            composable(Route.Measure.name) { SpO2MeasureScreen(navController) }
                        }
                    }
                }
            }
        }
    }

    private fun isPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            packageManager.checkPermission(it, packageName) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!WearableDataForegroundService.isRunning) PrivDataRequester.healthTrackingService.disconnectService()
    }
}
