package researchstack.presentation

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import researchstack.R
import researchstack.presentation.component.AppAlertDialog

@Composable
fun PermissionChecker(
    permissions: List<String> = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
    ),
    onPermissionResult: (isGranted: PermissionState) -> Unit
) {
    val context = LocalContext.current
    var reLauncherPermission by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) AppAlertDialog(
        onConfirmation = {
            showDialog = false
            reLauncherPermission += 1
        },
        onDismissRequest = {
            onPermissionResult(PermissionState.Denied)
            showDialog = false
        },
        dialogTitle = context.resources.getString(R.string.permission),
        dialogMessage = context.resources.getString(R.string.permission_require_dialog_description)
    )

    val permissionStates = permissions.map { permission ->
        context.packageManager.checkPermission(permission, context.packageName) == PERMISSION_GRANTED
    }.toMutableList()

    var targetLaunchSize by remember { mutableStateOf(permissions.size - permissionStates.filter { it }.size) }
    val launchers = List(permissions.size) { i ->
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (!permissionStates[i] && result) {
                permissionStates[i] = true
                targetLaunchSize -= 1
            } else if (!permissionStates[i] || !result) {
                if (context is Activity && ActivityCompat.shouldShowRequestPermissionRationale(
                        context,
                        permissions[i]
                    )
                ) {
                    showDialog = true
                } else onPermissionResult(PermissionState.Never)
            }
        }
    }

    LaunchedEffect(targetLaunchSize + reLauncherPermission) {
        if (targetLaunchSize == 0) {
            onPermissionResult(PermissionState.Granted)
        } else {
            val i = permissionStates.indexOfFirst { !it }
            if (i >= 0) {
                launchers[i].launch(permissions[i])
            }
        }
    }
}

fun requestScheduleExactAlarmPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            Intent().also { intent ->
                intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                intent.data = Uri.parse("package:" + context.applicationContext.packageName)
                context.startActivity(intent)
            }
        }
    }
}

enum class PermissionState {
    Initial,
    Denied,
    Never,
    Granted
}
