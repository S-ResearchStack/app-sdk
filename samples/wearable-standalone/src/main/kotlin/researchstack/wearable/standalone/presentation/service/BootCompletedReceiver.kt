package researchstack.wearable.standalone.presentation.service

import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class BootCompletedReceiver : DaggerBroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i(
            this::class.simpleName,
            "BootCompletedReceiver is triggered - Action: ${intent?.action ?: "unknown action"}"
        )
        runBlocking {
            // TODO Run Service according to PASSIVE_ON_OFF_PREF_KEY
            if (WearableDataForegroundService.isRunning.not())
                context.startForegroundService(Intent(context, WearableDataForegroundService::class.java))
        }
    }
}
