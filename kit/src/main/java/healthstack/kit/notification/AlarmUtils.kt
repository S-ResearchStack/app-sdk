package healthstack.kit.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import healthstack.kit.annotation.ForVerificationGenerated
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@ForVerificationGenerated
class AlarmUtils private constructor() {
    companion object {
        private lateinit var INSTANCE: AlarmUtils
        lateinit var alarmManager: AlarmManager

        const val REMINDER_ALARM_CODE = 0
        private val pendingIntentMap = mutableMapOf<Int, PendingIntent>()

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    pendingIntentMap[REMINDER_ALARM_CODE] =
                        PendingIntent.getBroadcast(
                            context,
                            REMINDER_ALARM_CODE,
                            Intent(context, TaskReminderAlarmReceiver::class.java),
                            PendingIntent.FLAG_IMMUTABLE
                        )

                    INSTANCE = AlarmUtils()
                }
            }
        }

        fun getInstance(): AlarmUtils = INSTANCE

        fun toTimeInMillis(time: String): Long {
            val format = SimpleDateFormat("hh:mm a", Locale.US)
            val timeWithoutDate = format.parse(time)
            val localDate = LocalDate.now()
            val calendar = Calendar.getInstance().apply {
                this.time = timeWithoutDate
                set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            if (calendar.timeInMillis < System.currentTimeMillis())
                calendar.add(Calendar.DAY_OF_MONTH, 1)

            return calendar.timeInMillis
        }
    }

    fun setRepeatingAlarm(triggerTime: Long, interval: Long, alarmCode: Int) {
        pendingIntentMap[alarmCode]?.let {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                interval,
                it
            )
        }
    }

    fun cancelAlarm(alarmCode: Int) {
        pendingIntentMap[alarmCode]?.let {
            alarmManager.cancel(it)
        }
    }
}
