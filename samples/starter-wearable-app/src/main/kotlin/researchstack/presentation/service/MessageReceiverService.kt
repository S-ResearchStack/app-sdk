package researchstack.presentation.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import com.google.gson.Gson
import com.google.gson.JsonParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.MessageConfig
import researchstack.MessageConfig.LAUNCH_APP_PATH
import researchstack.MessageConfig.SET_ECG_MEASUREMENT_ENABLED
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.usecase.PassiveDataStatusUseCase
import researchstack.domain.usecase.SetEcgMeasurementEnabledUseCase
import researchstack.domain.usecase.TrackDataUseCase
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MessageReceiverService : WearableListenerService() {
    private val gson = Gson()

    @Inject
    lateinit var passiveDataStatusUseCase: PassiveDataStatusUseCase

    @Inject
    lateinit var setEcgMeasurementEnabledUseCase: SetEcgMeasurementEnabledUseCase

    @Inject
    lateinit var trackDataUseCase: TrackDataUseCase

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d(TAG, "onMessageReceived")

        if (messageEvent.path == LAUNCH_APP_PATH) {
            val componentName = String(messageEvent.data)

            Log.i(TAG, "received : $componentName")

            val intent =
                if (hasActivityClass(componentName)) {
                    getIntentWithActivityClass(componentName)
                } else {
                    packageManager.getLaunchIntentForPackage(componentName)
                }

            if (intent != null) {
                startActivity(intent)
                vibrate()
            }
        }

        if (messageEvent.path == SET_ECG_MEASUREMENT_ENABLED) {
            val data = messageEvent.data.toString(Charsets.UTF_8).toBoolean()
            CoroutineScope(Dispatchers.IO).launch {
                setEcgMeasurementEnabledUseCase(data)
            }
        }

        if (messageEvent.path == MessageConfig.PASSIVE_DATA_STATUS_PATH) {
            val data = JsonParser.parseString(String(messageEvent.data))
            Log.i(TAG, "channel opened $data")
            val dataType = gson.fromJson(data.asJsonObject, PassiveDataStatusEntity::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                passiveDataStatusUseCase(PrivDataType.valueOf(dataType.dataType), dataType.enabled)
                if (!dataType.enabled) {
                    trackDataUseCase.deleteFile(PrivDataType.valueOf(dataType.dataType))
                }
            }
            Log.i(TAG, "channel opened $dataType")
        }
    }

    private fun hasActivityClass(componentName: String) = componentName.contains("/")

    private fun getIntentWithActivityClass(componentName: String): Intent {
        val (appPackageName, activityClassName, _) = componentName.split("/")
        return Intent().apply {
            component =
                ComponentName(
                    appPackageName,
                    if (activityClassName.startsWith('.')) appPackageName + activityClassName else activityClassName,
                )
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override fun onCapabilityChanged(capabilityInfo: CapabilityInfo) {
        super.onCapabilityChanged(capabilityInfo)
        Log.i(TAG, "onCapabilityChanged: onCapabilityChanged")
        if (capabilityInfo.nodes.isEmpty()) {
            Log.i(TAG, "onCapabilityChanged: onCapabilityChanged disConnect")
            alarmDeleteFile()
        } else {
            Log.i(TAG, "onCapabilityChanged: onCapabilityChanged connect")
            cancelAlarmDeleteFile()
        }
    }

    private fun alarmDeleteFile() {
        val am = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmToDeleteFile::class.java)
        alarmIntent.setAction(ACTION_DELETE_FILES)

        val alarmPendingIntent =
            PendingIntent.getBroadcast(
                this,
                DELETE_FILES_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_MUTABLE,
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) return
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + DELETE_TIME,
            alarmPendingIntent,
        )
    }

    private fun cancelAlarmDeleteFile() {
        val am = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, AlarmToDeleteFile::class.java)
        alarmIntent.setAction(ACTION_DELETE_FILES)

        val alarmPendingIntent =
            PendingIntent.getBroadcast(
                this,
                DELETE_FILES_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_MUTABLE,
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) return
        am.cancel(alarmPendingIntent)
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                500,
                VibrationEffect.DEFAULT_AMPLITUDE,
            ),
        )
    }

    companion object {
        private val TAG = MessageReceiverService::class.simpleName
        private const val ACTION_DELETE_FILES =
            "researchstack.presentation.service.ACTION_DELETE_FILES"
        private const val DELETE_FILES_REQUEST_CODE = 4
        private val DELETE_TIME = TimeUnit.DAYS.toMillis(120)
    }
}
