package researchstack.presentation.util

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
fun ignoreBatteryOptimization(context: Context) {
    val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager
    if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) return
    context.startActivity(
        Intent().apply {
            action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            data = Uri.parse("package:" + context.packageName)
        }
    )
}
