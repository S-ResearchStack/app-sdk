package researchstack.data.repository.wearable

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.MessageConfig.LAUNCH_APP_PATH
import researchstack.domain.repository.WearableMessageSenderRepository
import researchstack.util.getCapabilityInfo

class WearableMessageSenderRepositoryImpl(private val context: Context) :
    WearableMessageSenderRepository {

    override suspend fun sendLaunchAppMessage(
        message: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val capabilityInfo = getCapabilityInfo(context)
            if (capabilityInfo.nodes.size != 1) throw IllegalStateException("Node size is not 1")

            Tasks.await(
                Wearable.getMessageClient(context).sendMessage(
                    capabilityInfo.nodes.first().id,
                    LAUNCH_APP_PATH,
                    message.toByteArray()
                )
            )
            return@runCatching
        }
    }.onFailure {
        Log.d(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    companion object {
        private val TAG = WearableMessageSenderRepositoryImpl::class.simpleName
        private const val LAUNCH_APP_CAPABILITY_NAME = "launch_app_capability"
    }
}
