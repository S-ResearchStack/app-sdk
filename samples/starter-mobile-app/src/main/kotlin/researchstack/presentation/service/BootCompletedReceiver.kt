package researchstack.presentation.service

import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import researchstack.domain.usecase.sensor.GetPermittedSensorTypesUseCase
import researchstack.util.setAlarm
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : DaggerBroadcastReceiver() {
    @Inject
    lateinit var getPermittedSensorTypesUseCase: GetPermittedSensorTypesUseCase
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        runBlocking {
            if (getPermittedSensorTypesUseCase.invoke().first().isNotEmpty())
                context.startForegroundService(Intent(context, TrackerDataForegroundService::class.java))
        }
        setAlarm(context)
    }
}
