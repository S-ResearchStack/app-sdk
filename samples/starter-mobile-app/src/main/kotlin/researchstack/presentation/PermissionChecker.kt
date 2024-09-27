package researchstack.presentation

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Environment
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

@Composable
fun PermissionChecker(
    permissions: List<String> = listOf(),
    intents: List<String> = listOf(),
    onAllLaunched: () -> Unit,
) {
    val context = LocalContext.current

    val permissionStates = permissions.map { permission ->
        context.packageManager.checkPermission(permission, context.packageName) == PERMISSION_GRANTED
    }.toMutableList()

    val intentStates = intents.map { intent ->
        when (intent) {
            Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()
            else -> false
        }
    }.toMutableList()

    var targetLaunchSize by remember { mutableStateOf(permissions.size + intentStates.size - permissionStates.filter { it }.size - intentStates.filter { it }.size) }
    val launchers = List(permissions.size) { i ->
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (!permissionStates[i] && result) {
                permissionStates[i] = true
                targetLaunchSize -= 1
            }
        }
    }

    val startActivityLauncher = List(intentStates.size) { i ->
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) onResult@{ activityResult ->
            if (intents[i] == Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION) {
                if (activityResult.resultCode == Activity.RESULT_OK || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager())) {
                    if (!intentStates[i]) {
                        intentStates[i] = true
                        targetLaunchSize -= 1
                    }
                    return@onResult
                }
            }
        }
    }

    LaunchedEffect(targetLaunchSize) {
        if (targetLaunchSize == 0) {
            onAllLaunched()
        } else {
            val intentSelectedIndex = intentStates.indexOfFirst { !it }
            if (intentSelectedIndex >= 0) {
                startActivityLauncher[intentSelectedIndex].launch(Intent(intents[intentSelectedIndex]))
            } else {
                val permissionSelectedIndex = permissionStates.indexOfFirst { !it }
                if (permissionSelectedIndex >= 0) {
                    launchers[permissionSelectedIndex].launch(permissions[permissionSelectedIndex])
                }
            }
        }
    }
}
