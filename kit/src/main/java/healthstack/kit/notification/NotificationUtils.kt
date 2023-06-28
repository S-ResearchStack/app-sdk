package healthstack.kit.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC
import healthstack.kit.R
import healthstack.kit.annotation.ForVerificationGenerated

@ForVerificationGenerated
class NotificationUtils private constructor() {
    companion object {
        private lateinit var INSTANCE: NotificationUtils
        lateinit var notificationManager: NotificationManager

        const val REMINDER_NOTIFICATION = "reminder"

        // Add channels with unique ID
        private val channelIds = listOf(REMINDER_NOTIFICATION)
        private val notificationBuilderMap = mutableMapOf<String, NotificationCompat.Builder>()

        fun initialize(context: Context) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    channelIds.forEach {
                        notificationManager.createNotificationChannel(
                            NotificationChannel(it, it, NotificationManager.IMPORTANCE_DEFAULT)
                        )
                        notificationBuilderMap[it] = NotificationCompat.Builder(context, it)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setVisibility(VISIBILITY_PUBLIC)
                            .setShowWhen(true)
                    }
                    INSTANCE = NotificationUtils()
                }
            }
        }

        fun getInstance(): NotificationUtils = INSTANCE
    }

    fun notify(channelId: String, notificationId: Int, time: Long, title: String, text: String) {
        val builder =
            (notificationBuilderMap[channelId] ?: throw IllegalArgumentException("no such notification channel"))
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(time)

        notificationManager.notify(notificationId, builder.build())
    }
}
