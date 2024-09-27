package researchstack.data.repository.wearable

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.MessageConfig.PASSIVE_DATA_STATUS_PATH
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.repository.WearablePassiveDataStatusSenderRepository
import researchstack.util.getCapabilityInfo

class WearablePassiveDataStatusSenderRepositoryImpl(private val context: Context) :
    WearablePassiveDataStatusSenderRepository {
    private val gson = Gson()
    override suspend fun sendPassiveDataStatus(
        passiveDataType: Enum<*>,
        status: Boolean
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val capabilityInfo = getCapabilityInfo(context)
            val passiveDataStatus = PassiveDataStatusEntity(passiveDataType.name, status)
            Log.i(TAG, "sendPassiveDataStatus: ${passiveDataStatus.dataType}  ${passiveDataStatus.enabled}")
            if (capabilityInfo.nodes.size != 1) throw IllegalStateException("Node size is not 1")

            Tasks.await(
                Wearable.getMessageClient(context).sendMessage(
                    capabilityInfo.nodes.first().id,
                    PASSIVE_DATA_STATUS_PATH,
                    gson.toJson(passiveDataStatus).toByteArray()
                )
            )
            return@runCatching
        }
    }.onFailure {
        Log.d(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    companion object {
        private val TAG = WearablePassiveDataStatusSenderRepositoryImpl::class.simpleName
    }
}
