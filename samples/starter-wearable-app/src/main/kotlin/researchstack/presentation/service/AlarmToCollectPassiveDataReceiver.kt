package researchstack.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmToCollectPassiveDataReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val i = Intent(context, WearableDataForegroundService::class.java)
        intent?.let {
            i.setAction(intent.action)
            context.startForegroundService(i)
        }
    }
}
