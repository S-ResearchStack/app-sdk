package researchstack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import researchstack.R
import researchstack.presentation.initiate.MainActivity

class NotificationUtil private constructor() {
    companion object {
        private lateinit var INSTANCE: NotificationUtil
        lateinit var notificationManager: NotificationManager

        const val REMINDER_NOTIFICATION = "reminder"

        private val channelIds = listOf(REMINDER_NOTIFICATION)
        private val notificationBuilderMap = mutableMapOf<String, NotificationCompat.Builder>()
        private lateinit var pendingIntent: PendingIntent

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    channelIds.forEach {
                        notificationManager.createNotificationChannel(
                            NotificationChannel(it, it, NotificationManager.IMPORTANCE_DEFAULT).apply {
                                enableVibration(true)
                                vibrationPattern = longArrayOf(100, 200, 100, 200, 100, 400, 100, 400)
                            }
                        )
                        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

                        notificationBuilderMap[it] = NotificationCompat.Builder(context, it)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setVisibility(VISIBILITY_PUBLIC)
                            .setShowWhen(true)
                            .setSound(uri)
                    }
                    INSTANCE = NotificationUtil()

                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("page", 1)

                    pendingIntent = TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(intent)
                        getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                    }
                }
            }
        }

        fun getInstance(): NotificationUtil = INSTANCE
    }

    fun notify(channelId: String, notificationId: Int, time: Long, title: String, text: String) {
        val builder =
            (notificationBuilderMap[channelId] ?: throw IllegalArgumentException("no such notification channel"))
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(time)
                .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, builder.build())
    }

    fun cancelAllNotification() {
        notificationManager.cancelAll()
    }
}
