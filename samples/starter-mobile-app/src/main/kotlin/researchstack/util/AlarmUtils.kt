package researchstack.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class AlarmUtils private constructor() {
    companion object {
        private lateinit var INSTANCE: AlarmUtils
        lateinit var alarmManager: AlarmManager

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    INSTANCE = AlarmUtils()
                }
            }
        }

        fun getInstance(): AlarmUtils = INSTANCE
    }

    fun setRepeatingAlarm(context: Context, triggerTime: Long, interval: Long, alarmCode: Int) {
        val event = PendingIntent.getBroadcast(
            context,
            alarmCode,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, interval, event)
    }
}

fun setAlarm(context: Context) {
    val alarmManager = AlarmUtils.initialize(context).let { AlarmUtils.getInstance() }

    alarmManager.setRepeatingAlarm(context, hourToTimestamp(9), 1000 * 60 * 60 * 24, 0)
    alarmManager.setRepeatingAlarm(context, hourToTimestamp(12), 1000 * 60 * 60 * 24, 1)
    alarmManager.setRepeatingAlarm(context, hourToTimestamp(15), 1000 * 60 * 60 * 24, 2)
    alarmManager.setRepeatingAlarm(context, hourToTimestamp(18), 1000 * 60 * 60 * 24, 3)
}
