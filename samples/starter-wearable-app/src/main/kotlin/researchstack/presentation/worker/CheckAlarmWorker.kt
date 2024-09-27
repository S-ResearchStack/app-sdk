package researchstack.presentation.worker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import researchstack.presentation.service.AlarmToCollectPassiveDataReceiver
import researchstack.presentation.service.WearableDataForegroundService.Companion.COLLECT_STABLE_PASSIVE_DATA
import researchstack.presentation.service.WearableDataForegroundService.Companion.DURATION
import researchstack.presentation.service.WearableDataForegroundService.Companion.PERIOD
import researchstack.presentation.service.WearableDataForegroundService.Companion.STABLE_CHECK_DURATION
import researchstack.presentation.service.WearableDataForegroundService.Companion.START_COLLECT_PASSIVE_DATA_AFTER_PERIOD
import researchstack.presentation.service.WearableDataForegroundService.Companion.STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION

@HiltWorker
class CheckAlarmWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (PERIOD > DURATION) {
            if (!checkAlarm(START_COLLECT_PASSIVE_DATA_AFTER_PERIOD) && !checkAlarm(
                    STOP_COLLECT_PASSIVE_DATA_AFTER_DURATION
                )
            ) {
                triggerAlarm(START_COLLECT_PASSIVE_DATA_AFTER_PERIOD, PERIOD - DURATION)
            }
        } else {
            if (!checkAlarm(COLLECT_STABLE_PASSIVE_DATA)) {
                triggerAlarm(COLLECT_STABLE_PASSIVE_DATA, STABLE_CHECK_DURATION)
            }
        }
        return Result.success()
    }

    private fun checkAlarm(action: String): Boolean {
        val alarmIntent = Intent(applicationContext, AlarmToCollectPassiveDataReceiver::class.java)
        alarmIntent.setAction(action)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            alarmIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_MUTABLE
        )
        Log.d("CheckAlarmWorker", "alarm $action: ${alarmPendingIntent != null}")
        return alarmPendingIntent != null
    }

    private fun triggerAlarm(action: String, triggerTime: Int) {
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
}
