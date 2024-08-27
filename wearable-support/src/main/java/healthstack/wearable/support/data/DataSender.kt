package healthstack.wearable.support.data

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import healthstack.common.MessageConfig.MOBILE_RESEARCH_APP_CAPABILITY
import healthstack.common.model.WearDataType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DataSender(private val context: Context) {
    suspend fun sendData(
        serializableData: Any,
        wearDataType: WearDataType,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val nodes = getCapabilityNodes()
            if (nodes.size != 1) throw IllegalStateException("The nodes size is not 1. Node Size: ${nodes.size}")

            val channelClient = Wearable.getChannelClient(context)
            val channel = Tasks.await(channelClient.openChannel(nodes.first().id, wearDataType.messagePath))
            val message = Gson().toJson(serializableData).toByteArray()

            Tasks.await(channelClient.getOutputStream(channel)).use { it.write(message) }
        }.onFailure {
            Log.d(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    suspend fun isConnected(): Boolean {
        return getCapabilityNodes().size == 1
    }

    private fun getCapabilityNodes(): List<Node> {
        val capabilityClient = Wearable.getCapabilityClient(context.applicationContext)
        val capabilityInfoTask = capabilityClient.getCapability(
            MOBILE_RESEARCH_APP_CAPABILITY, CapabilityClient.FILTER_REACHABLE
        )
        return Tasks.await(capabilityInfoTask, 5, TimeUnit.SECONDS).nodes.toList()
    }

    companion object {
        private val TAG = DataSender::class.simpleName
    }
}
