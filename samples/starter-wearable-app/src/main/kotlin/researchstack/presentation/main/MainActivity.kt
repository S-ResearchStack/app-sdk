package researchstack.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.data.local.pref.PrivDataOnOffPref
import researchstack.data.local.pref.PrivDataOnOffPref.Companion.PERMITTED_DATA_PREF_KEY
import researchstack.data.local.pref.dataStore
import researchstack.data.repository.PrivDataRequester
import researchstack.domain.model.priv.PrivDataType
import researchstack.presentation.PermissionChecker
import researchstack.presentation.PermissionState
import researchstack.presentation.component.AppAlertDialog
import researchstack.presentation.main.screen.HomeScreen
import researchstack.presentation.requestScheduleExactAlarmPermission
import researchstack.presentation.service.WearableDataForegroundService
import researchstack.presentation.theme.HealthWearableTheme
import researchstack.presentation.worker.CheckAlarmWorker
import researchstack.presentation.worker.SyncPrivDataWorker
import researchstack.presentation.worker.SyncWatchEventDataWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerSyncPrivDataWorker()
        registerSyncWatchEventDataWorker()
        permitAllPrivDataTypes() // Delete after developing permission management

        setContent {
            HealthWearableTheme {
                var isHealthTrackerConnected by remember { mutableStateOf(PrivDataRequester.isConnected) }
                var permissionState by remember { mutableStateOf(PermissionState.Initial) }
                requestScheduleExactAlarmPermission(context)
                if (!isHealthTrackerConnected) {
                    PrivDataRequester.initialize(
                        this, applicationContext, onConnectionSuccess = { isHealthTrackerConnected = true }
                    )
                    PrivDataRequester.healthTrackingService.connectService()
                } else when (permissionState) {
                    PermissionState.Initial -> {
                        PermissionChecker {
                            permissionState = it
                        }
                    }

                    PermissionState.Granted -> {
                        startForegroundServiceIfNecessary()
                        registerCheckAlarmWorker()
                        HomeScreen(context = context)
                    }

                    PermissionState.Denied -> {
                        Toast.makeText(
                            this,
                            context.resources.getString(R.string.permission_denied),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }

                    PermissionState.Never -> {
                        AppAlertDialog(
                            onConfirmation = {
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    addCategory(Intent.CATEGORY_DEFAULT)
                                    data = Uri.parse("package:" + context.packageName)
                                    startActivity(this)
                                }
                                finish()
                            },
                            dialogTitle = context.resources.getString(R.string.permission),
                            dialogMessage = context.resources.getString(R.string.permission_never_ask_again_dialog_description),
                            confirmButtonText = context.resources.getString(R.string.settings),
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (WearableDataForegroundService.isRunning.not()) PrivDataRequester.healthTrackingService.disconnectService()
    }

    private fun startForegroundServiceIfNecessary() {
        CoroutineScope(Dispatchers.IO).launch {
            // Fix after developing permission management
            /*            PrivDataType.values().forEach { PrivDataOnOffPref(context, PERMITTED_DATA_PREF_KEY).add(it) }

                        if (PrivDataOnOffPref(context, PASSIVE_ON_OFF_PREF_KEY).privDataTypesFlow.first().isNotEmpty() &&
                            WearableDataForegroundService.isRunning.not()
                        ) */
            context.startForegroundService(Intent(context, WearableDataForegroundService::class.java))
        }
    }

    // Delete after developing permission management
    private fun permitAllPrivDataTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            PrivDataType.values().forEach { PrivDataOnOffPref(context.dataStore, PERMITTED_DATA_PREF_KEY).add(it) }
        }
    }

    private fun registerSyncPrivDataWorker() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncPrivDataWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<SyncPrivDataWorker>(1, TimeUnit.HOURS).build()
        )
    }

    private fun registerCheckAlarmWorker() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            CheckAlarmWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<CheckAlarmWorker>(15, TimeUnit.MINUTES).setInitialDelay(1, TimeUnit.MINUTES).build()
        )
    }

    private fun registerSyncWatchEventDataWorker() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncWatchEventDataWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<SyncWatchEventDataWorker>(1, TimeUnit.HOURS).build()
        )
    }

    companion object {
        const val PPG_BUNDLE_KEY = "ppg_key"
    }
}
