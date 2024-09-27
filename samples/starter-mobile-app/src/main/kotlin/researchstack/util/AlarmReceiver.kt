package researchstack.util

import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.presentation.service.DaggerBroadcastReceiver
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var taskDao: TaskDao

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        runBlocking {
            NotificationUtil.initialize(context)
            val notificationUtils = NotificationUtil.getInstance()

            val active = taskDao.getActiveTasks(LocalDateTime.now().toString()).first().filter { !it.task.inClinic }
            if (active.isNotEmpty()) {
                notificationUtils.notify(
                    NotificationUtil.REMINDER_NOTIFICATION,
                    Random.nextInt(),
                    System.currentTimeMillis(),
                    "Time for your daily tasks",
                    "Please take some time to complete your daily tasks"
                )
            }
        }
    }
}
