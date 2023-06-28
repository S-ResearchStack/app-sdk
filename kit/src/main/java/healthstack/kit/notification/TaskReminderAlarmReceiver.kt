package healthstack.kit.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.random.Random

class TaskReminderAlarmReceiver : BroadcastReceiver() {
    private val notificationUtils = NotificationUtils.getInstance()

    override fun onReceive(context: Context?, intent: Intent?) {
        notificationUtils.notify(
            NotificationUtils.REMINDER_NOTIFICATION,
            Random.nextInt(),
            System.currentTimeMillis(),
            "It's time for your daily assessment",
            "Please take some time to complete your daily assessment in the next 8 hours."
        )
    }
}
