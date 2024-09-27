package researchstack.domain.usecase

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.events.WearablePowerState
import researchstack.domain.repository.DataSenderRepository
import researchstack.domain.repository.WearableEventRepository
import javax.inject.Inject

class SyncWatchEventDataUseCase @Inject constructor(
    private val dataSenderRepository: DataSenderRepository,
    private val wearableBatteryFileRepository: WearableEventRepository<WearableBattery>,
    private val wearableOffBodyFileRepository: WearableEventRepository<WearableOffBody>,
    private val wearablePowerStateEventFileRepository: WearableEventRepository<WearablePowerState>,
) {
    suspend operator fun invoke() = runCatching {
        if (!dataSenderRepository.isConnected()) throw IllegalStateException("not connected with mobile")
        wearableBatteryFileRepository.syncFile()
        wearableOffBodyFileRepository.syncFile()
        wearablePowerStateEventFileRepository.syncFile()
    }

    private suspend fun WearableEventRepository<*>.syncFile() = runCatching {
        val channelQueue = Channel<Boolean>()
        getCompletedFiles().forEach { file ->
            dataSenderRepository.sendFile(file) { isSuccess ->
                Log.i(SyncWatchEventDataUseCase::class.simpleName, "Succeed to send ${file.name}")
                if (isSuccess) {
                    file.delete()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    channelQueue.send(element = true)
                }
            }
            channelQueue.receive()
        }
        channelQueue.close()
    }
}
