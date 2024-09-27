package researchstack.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import researchstack.R
import researchstack.data.datasource.local.room.dao.LightDao
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.domain.model.sensor.Accelerometer
import researchstack.domain.model.sensor.Light
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.usecase.sensor.GetPermittedSensorTypesUseCase
import researchstack.domain.usecase.sensor.accelerometer.SaveAcceleroDataUseCase
import researchstack.domain.usecase.sensor.accelerometer.StopTrackingAcceleroDataUseCase
import researchstack.domain.usecase.sensor.accelerometer.TrackingAcceleroDataUseCase
import researchstack.domain.usecase.sensor.light.SaveLightDataUseCase
import researchstack.domain.usecase.sensor.light.StopTrackingLightDataUseCase
import researchstack.domain.usecase.sensor.light.TrackLightDataUseCase
import researchstack.domain.usecase.sensor.speed.SaveSpeedDataUseCase
import researchstack.domain.usecase.sensor.speed.StopTrackingSpeedDataUseCase
import researchstack.domain.usecase.sensor.speed.TrackSpeedDataUseCase
import researchstack.presentation.initiate.MainActivity
import java.util.Collections
import javax.inject.Inject

@AndroidEntryPoint
class TrackerDataForegroundService : Service() {

    @Inject
    lateinit var getPermittedSensorTypesUseCase: GetPermittedSensorTypesUseCase

    @Inject
    lateinit var trackingLightDataUseCase: TrackLightDataUseCase

    @Inject
    lateinit var stopTrackingLightDataUseCase: StopTrackingLightDataUseCase

    @Inject
    lateinit var saveLightDataUseCase: SaveLightDataUseCase

    @Inject
    lateinit var trackSpeedDataUseCase: TrackSpeedDataUseCase

    @Inject
    lateinit var stopTrackSpeedDataUseCase: StopTrackingSpeedDataUseCase

    @Inject
    lateinit var saveSpeedDataUseCase: SaveSpeedDataUseCase

    @Inject
    lateinit var trackingAcceleroDataUseCase: TrackingAcceleroDataUseCase

    @Inject
    lateinit var stopTrackingAcceleroDataUseCase: StopTrackingAcceleroDataUseCase

    @Inject
    lateinit var saveAcceleroDataUseCase: SaveAcceleroDataUseCase

    @Inject
    lateinit var lightDao: LightDao

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val lights = Collections.synchronizedList(mutableListOf<Light>())
    private val accelerometers = Collections.synchronizedList(mutableListOf<Accelerometer>())
    private val speeds = Collections.synchronizedList(mutableListOf<Speed>())

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            PENDING_INTENT_REQUEST_CODE,
            notificationIntent,
            PendingIntent.FLAG_MUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Health Research Service")
            .setSmallIcon(R.drawable.ic_launcher_playstore)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(SERVICE_ID, notification)

        startTracking()
        storeSensorData()

        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "health-research::TrackerDataForegroundService").apply {
                    acquire()
                }
            }

        return START_REDELIVER_INTENT
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

    private fun startTracking() {
        coroutineScope.launch {
            getPermittedSensorTypesUseCase().collect { permittedSensorTypes ->
                Log.i(
                    TrackerDataForegroundService::class.simpleName,
                    "Changes PermittedSensorTypes: $permittedSensorTypes"
                )
                permittedSensorTypes.forEach { it.startTracking() }

                TrackerDataType.values().filterNot { permittedSensorTypes.contains(it) }
                    .forEach {
                        Log.i(TrackerDataForegroundService::class.simpleName, "Stop Tracking: $it")
                        it.stopTracking()
                        it.store()
                    }
            }
        }
    }

    private fun storeSensorData() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(FLUSH_INTERVAL)
                store()
            }
        }
    }

    private fun TrackerDataType.startTracking() = coroutineScope.launch {
        when (this@startTracking) {
            TrackerDataType.LIGHT -> trackingLightDataUseCase().collect {
                Log.d(TrackerDataForegroundService::class.simpleName, "LIGHT ADDED $it")
                lights.add(it)
            }

            TrackerDataType.ACCELEROMETER -> trackingAcceleroDataUseCase().collect {
                Log.d(TrackerDataForegroundService::class.simpleName, "ACCELEROMETER ADDED $it")
                accelerometers.add(it)
            }

            TrackerDataType.SPEED -> trackSpeedDataUseCase().collect {
                Log.d(TrackerDataForegroundService::class.simpleName, "SPEED ADDED $it")
                speeds.add(it)
            }
        }
    }

    private fun TrackerDataType.stopTracking() = when (this) {
        TrackerDataType.LIGHT -> stopTrackingLightDataUseCase()
        TrackerDataType.ACCELEROMETER -> stopTrackingAcceleroDataUseCase()
        TrackerDataType.SPEED -> stopTrackSpeedDataUseCase()
    }

    private suspend fun store() = getPermittedSensorTypesUseCase().first().forEach { it.store() }

    private fun TrackerDataType.store() = coroutineScope.launch {
        when (this@store) {
            TrackerDataType.LIGHT -> {
                val toStore = lights.toList()
                lights.clear()
                Log.i(TrackerDataForegroundService::class.simpleName, "Save Light : ${toStore.size}")
                saveLightDataUseCase(toStore)
            }

            TrackerDataType.ACCELEROMETER -> {
                val toStore = accelerometers.toList()
                accelerometers.clear()
                saveAcceleroDataUseCase(toStore)
            }

            TrackerDataType.SPEED -> {
                val toStore = speeds.toList()
                speeds.clear()
                saveSpeedDataUseCase(toStore)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        runBlocking { store() }
        wakeLock.release()
        coroutineScope.cancel()
    }

    companion object {
        private const val CHANNEL_ID = "SENSOR_FOREGROUND_SERVICE_NOTIFICATION_CHANNEL"
        private const val SERVICE_ID = 1
        private const val PENDING_INTENT_REQUEST_CODE = 0
        private const val FLUSH_INTERVAL = 1000L * 60 * 1
    }
}
