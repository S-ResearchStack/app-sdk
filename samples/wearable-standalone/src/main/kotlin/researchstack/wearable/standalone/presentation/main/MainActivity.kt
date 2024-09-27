package researchstack.wearable.standalone.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.data.local.pref.PrivDataOnOffPref
import researchstack.data.repository.PrivDataRequester
import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.data.local.pref.dataStore
import researchstack.wearable.standalone.presentation.PermissionChecker
import researchstack.wearable.standalone.presentation.PermissionState
import researchstack.wearable.standalone.presentation.component.AppAlertDialog
import researchstack.wearable.standalone.presentation.main.screen.HomeScreen
import researchstack.wearable.standalone.presentation.service.WearableDataForegroundService
import researchstack.wearable.standalone.presentation.theme.HealthWearableTheme
import researchstack.wearable.standalone.presentation.viewmodel.SplashLoadingViewModel
import researchstack.wearable.standalone.presentation.worker.SyncPrivDataWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val context = this
    private var isContentReady: Boolean = false

    private lateinit var splashLoadingViewModel: SplashLoadingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerSyncPrivDataWorker()
        permitAllPrivDataTypes() // Delete after developing permission management

        initSplashLoadingViewModel()

        setContent {
            HealthWearableTheme {
                var isHealthTrackerConnected by remember { mutableStateOf(PrivDataRequester.isConnected) }
                var permissionState by remember { mutableStateOf(PermissionState.Initial) }
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
                        if (splashLoadingViewModel.isReady.collectAsState().value) {
                            startForegroundServiceIfNecessary()
                            HomeScreen(context = context)
                        } else {
                            splashLoadingViewModel.createAccount()
                        }
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

    private fun initSplashLoadingViewModel() {
        splashLoadingViewModel = ViewModelProvider(this)[SplashLoadingViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        if (WearableDataForegroundService.isRunning.not()) PrivDataRequester.healthTrackingService.disconnectService()
    }

    private fun startForegroundServiceIfNecessary() {
        CoroutineScope(Dispatchers.IO).launch {
            context.startForegroundService(Intent(context, WearableDataForegroundService::class.java))
        }
    }

    // Delete after developing permission management
    private fun permitAllPrivDataTypes() {
        CoroutineScope(Dispatchers.IO).launch {
            PrivDataType.values().forEach { PrivDataOnOffPref(context.dataStore, PrivDataOnOffPref.PERMITTED_DATA_PREF_KEY).add(it) }
        }
    }

    private fun registerSyncPrivDataWorker() {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncPrivDataWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.UPDATE,
            PeriodicWorkRequestBuilder<SyncPrivDataWorker>(1, TimeUnit.HOURS).build()
        )
    }

    companion object {
        const val PPG_BUNDLE_KEY = "ppg_key"
    }
}
