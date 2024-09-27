package researchstack.presentation.service

import android.Manifest
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
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
import researchstack.BuildConfig
import researchstack.R
import researchstack.data.repository.PrivDataRequester
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.WearableEventRepository
import researchstack.domain.usecase.PassiveDataStatusUseCase
import researchstack.presentation.main.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class WearableDataForegroundService : Service() {
    @Inject
    lateinit var passiveDataTracker: PassiveDataTracker

    @Inject
    lateinit var passiveDataStatusUseCase: PassiveDataStatusUseCase

    @Inject
    lateinit var wearableOffBodyRepository: WearableEventRepository<WearableOffBody>

    @Inject
    lateinit var wearableSensorManager: WearableSensorManager

    private lateinit var wearableBatteryChangeReceiver: WearableBatteryChangeReceiver

    private val requiredPermissions = listOf(
        Manifest.permission.ACTIVITY_RECOGNITION,
        Manifest.permission.BODY_SENSORS,
    )

    private lateinit var wearablePowerStateEventReceiver: WearablePowerStateEventReceiver

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

        registerWearPowerStateEventReceiver()

        startCollectPassiveData()
        CoroutineScope(Dispatchers.IO).launch {
            wearableSensorManager.startTracking().collect {
                wearableOffBodyRepository.insertAll(listOf(it))
            }
        }
        registerBatteryChangeReceiver()
    }

    private fun registerBatteryChangeReceiver() {
        wearableBatteryChangeReceiver = WearableBatteryChangeReceiver()
        val intentFilter = IntentFilter(Intent.ACTION_TIME_TICK)
        registerReceiver(wearableBatteryChangeReceiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i(TAG, "ForegroundService Start Command Intent ${intent?.action}")
        when (intent?.action) {
            START_COLLECT_PASSIVE_DATA_AFTER_PERIOD -> {
                startCollectPassiveData()
                alarmActionToCollectPassiveData(STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION, DURATION)
            }

            STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION -> {
                stopCollectPassiveData()
                alarmActionToCollectPassiveData(START_COLLECT_PASSIVE_DATA_AFTER_PERIOD, PERIOD - DURATION)
            }

            COLLECT_STABLE_PASSIVE_DATA -> {
                startCollectPassiveData()
                alarmActionToCollectPassiveData(COLLECT_STABLE_PASSIVE_DATA, STABLE_CHECK_DURATION)
            }

            else -> {
                if (PERIOD > DURATION) {
                    alarmActionToCollectPassiveData(
                        STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION,
                        DURATION
                    ) // send action stop to stop collect data for current times
                } else {
                    alarmActionToCollectPassiveData(COLLECT_STABLE_PASSIVE_DATA, STABLE_CHECK_DURATION)
                }
            }
        }

        // TODO: period, duration may not work
        return START_STICKY
    }

    private fun alarmActionToCollectPassiveData(action: String, triggerTime: Int) {
        val am = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(applicationContext, AlarmToCollectPassiveDataReceiver::class.java)
        alarmIntent.setAction(action)

        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            alarmIntent,
            PendingIntent.FLAG_MUTABLE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) return
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + triggerTime,
            alarmPendingIntent
        )
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

    private fun registerWearPowerStateEventReceiver() {
        wearablePowerStateEventReceiver = WearablePowerStateEventReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_SHUTDOWN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(wearablePowerStateEventReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(wearablePowerStateEventReceiver, intentFilter)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "ForegroundService onDestroy Command")
        stopCollectPassiveData()
        PrivDataRequester.healthTrackingService.disconnectService()
        unregisterReceiver(wearablePowerStateEventReceiver)
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
        const val PERIOD = BuildConfig.COLLECT_PASSIVE_DATA_PERIOD
        const val DURATION = BuildConfig.COLLECT_PASSIVE_DATA_DURATION
        const val STABLE_CHECK_DURATION = 10 * 60 * 1000 // 10 minute

        @Volatile
        var isRunning = false
            private set
    }
}
