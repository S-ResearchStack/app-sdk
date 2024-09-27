package researchstack.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.repository.WearableEventRepository
import javax.inject.Inject

@AndroidEntryPoint
class WearableBatteryChangeReceiver : BroadcastReceiver() {
    @Inject
    lateinit var wearableBatteryRepository: WearableEventRepository<WearableBattery>

    private var lastTimeBatteryLevel = -1

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(WearableBatteryChangeReceiver::class.simpleName, "on receiver")
        if (intent.action == Intent.ACTION_TIME_TICK) {
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context.registerReceiver(null, ifilter)
                }

            val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val charging = when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING, BatteryManager.BATTERY_STATUS_FULL -> 1
                BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_NOT_CHARGING -> 0
                else -> -1
            }
            val batteryPct: Long? = batteryStatus?.let { sIntent ->
                val level: Int = sIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                val scale: Int = sIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                level * 100 / scale.toLong()
            }
            Log.i(WearableBatteryChangeReceiver::class.simpleName, "$batteryPct , $status")
            if (batteryPct?.toInt() != lastTimeBatteryLevel) {
                lastTimeBatteryLevel = batteryPct?.toInt() ?: -1
                CoroutineScope(Dispatchers.IO).launch {
                    wearableBatteryRepository.insertAll(
                        listOf(
                            WearableBattery(
                                percentage = batteryPct ?: -1,
                                isCharging = charging
                            )
                        )
                    )
                }
            }
        }
    }
}
