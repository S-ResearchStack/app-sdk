package researchstack.wearable.standalone.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

abstract class DaggerBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {}
}
