package researchstack.util

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient.FILTER_REACHABLE
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import researchstack.MessageConfig.WEARABLE_RESEARCH_APP_CAPABILITY
import java.util.concurrent.TimeUnit

fun getCapabilityInfo(context: Context): CapabilityInfo {
    val capabilityClient = Wearable.getCapabilityClient(context)
    val capabilityInfoTask = capabilityClient.getCapability(
        WEARABLE_RESEARCH_APP_CAPABILITY, FILTER_REACHABLE
    )
    return Tasks.await(capabilityInfoTask, 5, TimeUnit.SECONDS)
}
