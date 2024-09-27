package researchstack.domain.usecase

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import researchstack.data.local.pref.PrivDataOnOffPref
import researchstack.data.local.pref.PrivDataOnOffPref.Companion.PERMITTED_DATA_PREF_KEY
import researchstack.data.local.pref.dataStore
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.repository.DataSenderRepository
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

@Suppress("LongParameterList")
class SyncPrivDataUseCase @Inject constructor(
    private val dataSenderRepository: DataSenderRepository,
    private val accMeterFileRepository: WearableDataRepository<Accelerometer>,
    private val biaFileRepository: WearableDataRepository<Bia>,
    private val ecgFileRepository: WearableDataRepository<EcgSet>,
    private val ppgGreenFileRepository: WearableDataRepository<PpgGreen>,
    private val ppgIrFileRepository: WearableDataRepository<PpgIr>,
    private val ppgRedFileRepository: WearableDataRepository<PpgRed>,
    private val spO2FileRepository: WearableDataRepository<SpO2>,
    private val sweatLossFileRepository: WearableDataRepository<SweatLoss>,
    private val heartRateFileRepository: WearableDataRepository<HeartRate>,
) {
    suspend operator fun invoke(context: Context) = runCatching {
        if (!dataSenderRepository.isConnected()) throw IllegalStateException("not connected with mobile")

        PrivDataOnOffPref(context.dataStore, PERMITTED_DATA_PREF_KEY).privDataTypesFlow.first().map {
            when (it) {
                PrivDataType.WEAR_ACCELEROMETER -> it.syncFile(accMeterFileRepository)
                PrivDataType.WEAR_BIA -> it.syncFile(biaFileRepository)
                PrivDataType.WEAR_ECG -> it.syncFile(ecgFileRepository)
                PrivDataType.WEAR_PPG_GREEN -> it.syncFile(ppgGreenFileRepository)
                PrivDataType.WEAR_PPG_IR -> it.syncFile(ppgIrFileRepository)
                PrivDataType.WEAR_PPG_RED -> it.syncFile(ppgRedFileRepository)
                PrivDataType.WEAR_SPO2 -> it.syncFile(spO2FileRepository)
                PrivDataType.WEAR_SWEAT_LOSS -> it.syncFile(sweatLossFileRepository)
                PrivDataType.WEAR_HEART_RATE -> it.syncFile(heartRateFileRepository)
            }
        }.find { it.isFailure }?.getOrThrow()
    }

    private suspend fun PrivDataType.syncFile(
        wearableDataRepository: WearableDataRepository<*>,
    ) = runCatching {
        val channelQueue = Channel<Boolean>()
        wearableDataRepository.getCompletedFiles().forEach { file ->
            dataSenderRepository.sendFile(file) { isSuccess ->
                Log.i(TAG, "Succeed to send ${file.name}")
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

    companion object {
        private val TAG = SyncPrivDataUseCase::class.simpleName
    }
}
