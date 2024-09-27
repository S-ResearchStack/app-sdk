package researchstack.wearable.standalone.presentation.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.data.repository.PrivDataRequester
import researchstack.domain.model.priv.PrivDataType
import researchstack.wearable.standalone.R
import researchstack.wearable.standalone.domain.usecase.PassiveDataStatusUseCase
import researchstack.wearable.standalone.presentation.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class WearableDataForegroundService : Service() {
    @Inject
    lateinit var passiveDataTracker: PassiveDataTracker

    @Inject
    lateinit var passiveDataStatusUseCase: PassiveDataStatusUseCase

    private val requiredPermissions = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
    )

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            PENDING_INTENT_REQUEST_CODE,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Research Ongoing")
            .setContentText("✏\uFE0F Keep watch on & notifications active")
            .setSmallIcon(R.drawable.ic_launcher_playstore)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(SERVICE_ID, notification)

        startCollectPassiveData()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG, "ForegroundService Start Command Intent ${intent?.action}")
        when (intent?.action) {
            START_COLLECT_PASSIVE_DATA_AFTER_PERIOD -> {
                startCollectPassiveData()
            }

            STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION -> {
                stopCollectPassiveData()
            }

            COLLECT_STABLE_PASSIVE_DATA -> {
                startCollectPassiveData()
            }
        }

        // TODO: period, duration may not work
        return START_STICKY
    }

    private var collectDataCoroutine = CoroutineScope(Dispatchers.IO)
    private val collectDataJob = collectDataCoroutine.launch(start = CoroutineStart.LAZY) {
        Log.d(TAG, "collecting wearable data job started")
        passiveDataStatusUseCase().collect { entities ->
            Log.d(TAG, "passive data status changed: $entities")
            passiveDataTracker.trackingAndSaveWearableData(
                entities.map {
                    PrivDataType.valueOf(
                        it.dataType
                    )
                }
            )
        }
    }

    private val collectStableDataJob = collectDataCoroutine.launch(start = CoroutineStart.LAZY) {
        Log.d(TAG, "collect stable data job")
        passiveDataStatusUseCase().first().let { entities ->
            Log.d(TAG, "passive data status: $entities")
            passiveDataTracker.trackingAndSaveWearableData(
                entities.map {
                    PrivDataType.valueOf(
                        it.dataType
                    )
                }
            )
        }
    }

    private fun startCollectPassiveData() {
        val job = collectDataJob.takeIf { !collectDataJob.isActive } ?: collectStableDataJob
        isRunning = true
        if (PrivDataRequester.isConnected) {
            job.takeIf { isPermissionsGranted() }?.start()
        } else {
            PrivDataRequester.initialize(
                null,
                applicationContext,
                onConnectionSuccess = {
                    job.takeIf { isPermissionsGranted() }?.start()
                }
            )
            PrivDataRequester.healthTrackingService.connectService()
        }
    }

    private fun isPermissionsGranted(): Boolean {
        return requiredPermissions.all {
            packageManager.checkPermission(
                it,
                packageName
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun stopCollectPassiveData() {
        runBlocking {
            passiveDataTracker.stopTracking()
        }
        collectDataCoroutine.cancel()
        isRunning = false
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "ForegroundService onDestroy Command")
        stopCollectPassiveData()
        PrivDataRequester.healthTrackingService.disconnectService()
        isRunning = false
    }

    companion object {
        private const val CHANNEL_ID = "PRIV_FOREGROUND_SERVICE_NOTIFICATION_CHANNEL"
        private const val SERVICE_ID = 1
        private const val PENDING_INTENT_REQUEST_CODE = 0
        private val TAG = WearableDataForegroundService::class.simpleName

        const val START_COLLECT_PASSIVE_DATA_AFTER_PERIOD =
            "researchstack.START_COLLECT_PASSIVE_DATA"
        const val STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION =
            "researchstack.STOP_COLLECT_PASSIVE_DATA"
        const val COLLECT_STABLE_PASSIVE_DATA =
            "researchstack.COLLECT_STABLE_PASSIVE_DATA"

        @Volatile
        var isRunning = false
            private set
    }
}
