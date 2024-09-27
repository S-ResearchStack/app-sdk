package researchstack.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.ChannelClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.DataTransferMessage
import researchstack.MessageConfig.MOBILE_RESEARCH_APP_CAPABILITY
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.DataSenderRepository
import java.io.File
import java.util.concurrent.TimeUnit

class ChannelDataSenderRepository(private val context: Context) : DataSenderRepository {
    override suspend fun sendData(
        serializableData: Any,
        privDataType: PrivDataType,
    ) = withContext(Dispatchers.IO) {
        runCatching {
            val nodes = getCapabilityNodes()
            if (nodes.size != 1) throw IllegalStateException("The nodes size is not 1. Node Size: ${nodes.size}")

            val channelClient = Wearable.getChannelClient(context)
            val channel = Tasks.await(channelClient.openChannel(nodes.first().id, privDataType.messagePath))
            val message = Gson().toJson(DataTransferMessage(privDataType, serializableData)).toByteArray()

            Tasks.await(channelClient.getOutputStream(channel)).use { it.write(message) }
        }.onFailure {
            Log.d(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    override suspend fun sendFile(file: File, onResult: (isSuccess: Boolean) -> Unit) = withContext(Dispatchers.IO) {
        runCatching {
            val nodes = getCapabilityNodes()
            if (nodes.size != 1) throw IllegalStateException("The nodes size is not 1. Node Size: ${nodes.size}")
            val channelClient = Wearable.getChannelClient(context)

            val channelCallback = object : ChannelClient.ChannelCallback() {
                override fun onChannelClosed(
                    channel: ChannelClient.Channel,
                    closeReason: Int,
                    p2: Int,
                ) {
                    super.onChannelClosed(channel, closeReason, p2)
                    Log.i(TAG, "onChannelClosed ${channel.path}, closeReason: $closeReason")
                    if (closeReason == CLOSE_REASON_REMOTE_CLOSE) {
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                    channelClient.unregisterChannelCallback(this)
                }
            }
            val channel = Tasks.await(channelClient.openChannel(nodes.first().id, file.name))
            Tasks.await(channelClient.registerChannelCallback(channel, channelCallback))
            Tasks.await(channelClient.sendFile(channel, Uri.fromFile(file)))
            return@withContext Result.success(Unit)
        }.onFailure {
            Log.d(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    override suspend fun isConnected(): Boolean {
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
        private val TAG = ChannelDataSenderRepository::class.simpleName
    }
}
