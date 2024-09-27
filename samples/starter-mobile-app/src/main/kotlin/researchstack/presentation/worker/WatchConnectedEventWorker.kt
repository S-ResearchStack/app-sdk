package researchstack.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.domain.model.events.MobileWearConnection
import researchstack.domain.repository.WatchEventsRepository
import researchstack.util.getCapabilityInfo

@HiltWorker
class WatchConnectedEventWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val watchConnectedEventRepository: WatchEventsRepository<MobileWearConnection>
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        kotlin.runCatching {
            val capabilityInfo = getCapabilityInfo(appContext)
            val deviceNames = capabilityInfo.nodes.joinToString(",") {
                "${it.displayName}-${it.id}"
            }

            watchConnectedEventRepository.insert(
                MobileWearConnection(wearableDeviceName = deviceNames)
            )
        }.onFailure {
            it.printStackTrace()
            return@withContext Result.failure()
        }
        return@withContext Result.success()
    }
}
