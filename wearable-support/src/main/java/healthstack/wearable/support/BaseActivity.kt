package healthstack.wearable.support

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import healthstack.wearable.kit.theme.HealthWearableTheme

enum class Route {
    Home,
    Main,
    Measure,
}

@Composable
fun BaseActivity(activity: Activity, healthDataList: List<String>) {
    HealthWearableTheme {
        var isHealthTrackerConnected by remember { mutableStateOf(PrivDataRequester.isConnected) }
        var isPermissionCheckerLaunched by remember { mutableStateOf(false) }

        if (!isHealthTrackerConnected || !isPermissionCheckerLaunched) {
            PrivDataRequester.initialize(
                activity, activity.applicationContext, onConnectionSuccess = { isHealthTrackerConnected = true }
            )
            PrivDataRequester.healthTrackingService.connectService()
            PermissionChecker { isPermissionCheckerLaunched = true }
        } else {
            Router(healthDataList)
        }
    }
}

val appPermissions = if (Build.VERSION.SDK_INT >= 33) listOf(
    Manifest.permission.ACTIVITY_RECOGNITION,
    Manifest.permission.BODY_SENSORS,
    Manifest.permission.POST_NOTIFICATIONS,
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
) else listOf(
    Manifest.permission.ACTIVITY_RECOGNITION,
    Manifest.permission.BODY_SENSORS,
    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
)

@Composable
fun PermissionChecker(
    permissions: List<String> = appPermissions,
    onAllLaunched: () -> Unit,
) {
    val context = LocalContext.current

    val permissionStates = permissions.map { permission ->
        context.packageManager.checkPermission(permission, context.packageName) == PERMISSION_GRANTED
    }.toMutableList()

    var targetLaunchSize by remember { mutableStateOf(permissions.size - permissionStates.filter { it }.size) }
    val launchers = List(permissions.size) { i ->
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            permissionStates[i] = result
            targetLaunchSize -= 1
        }
    }

    LaunchedEffect(targetLaunchSize) {
        if (targetLaunchSize == 0) {
            onAllLaunched()
        } else {
            val i = permissionStates.indexOfFirst { !it }
            if (i >= 0) {
                launchers[i].launch(permissions[i])
            }
        }
    }
}
