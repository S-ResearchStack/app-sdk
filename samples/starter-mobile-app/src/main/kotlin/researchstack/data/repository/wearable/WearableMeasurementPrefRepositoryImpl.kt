package researchstack.data.repository.wearable

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.MessageConfig.SET_ECG_MEASUREMENT_ENABLED
import researchstack.domain.repository.WearableMeasurementPrefRepository
import researchstack.util.getCapabilityInfo

class WearableMeasurementPrefRepositoryImpl(private val context: Context) : WearableMeasurementPrefRepository {
    override suspend fun setEcgMeasurementEnabled(enabled: Boolean): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val capabilityInfo = getCapabilityInfo(context)
                if (capabilityInfo.nodes.size != 1) throw IllegalStateException("Node size is not 1")
                Tasks.await(
                    Wearable.getMessageClient(context).sendMessage(
                        capabilityInfo.nodes.first().id,
                        SET_ECG_MEASUREMENT_ENABLED,
                        enabled.toString().toByteArray()
                    )
                )
                return@runCatching
            }
        }.onFailure {
            Log.d(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }

    companion object {
        private val TAG = WearableMeasurementPrefRepositoryImpl::class.simpleName
    }
}
