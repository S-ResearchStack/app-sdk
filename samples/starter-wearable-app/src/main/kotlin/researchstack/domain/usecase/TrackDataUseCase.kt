package researchstack.domain.usecase

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.Timestamp
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
import researchstack.domain.repository.WearableDataRepository
import javax.inject.Inject

class TrackDataUseCase @Inject constructor(
    private val accMeterRepository: WearableDataRepository<Accelerometer>,
    private val biaRepository: WearableDataRepository<Bia>,
    private val ecgRepository: WearableDataRepository<EcgSet>,
    private val ppgGreenRepository: WearableDataRepository<PpgGreen>,
    private val ppgIrRepository: WearableDataRepository<PpgIr>,
    private val ppgRedRepository: WearableDataRepository<PpgRed>,
    private val spO2Repository: WearableDataRepository<SpO2>,
    private val sweatLossRepository: WearableDataRepository<SweatLoss>,
    private val heartRateRepository: WearableDataRepository<HeartRate>,
) {
    private fun getPrivRepository(privDataType: PrivDataType): WearableDataRepository<out Timestamp> {
        return when (privDataType) {
            PrivDataType.WEAR_ACCELEROMETER -> accMeterRepository
            PrivDataType.WEAR_BIA -> biaRepository
            PrivDataType.WEAR_ECG -> ecgRepository
            PrivDataType.WEAR_PPG_GREEN -> ppgGreenRepository
            PrivDataType.WEAR_PPG_IR -> ppgIrRepository
            PrivDataType.WEAR_PPG_RED -> ppgRedRepository
            PrivDataType.WEAR_SPO2 -> spO2Repository
            PrivDataType.WEAR_SWEAT_LOSS -> sweatLossRepository
            PrivDataType.WEAR_HEART_RATE -> heartRateRepository
            else -> heartRateRepository
        }
    }

    fun startTracking(privDataType: PrivDataType): Flow<Timestamp> {
        val privRepository = getPrivRepository(privDataType)
        return privRepository.startTracking()
    }

    suspend fun stopTracking(privDataType: PrivDataType): Result<Unit> {
        val privRepository = getPrivRepository(privDataType)
        return privRepository.stopTracking()
    }

    fun sendData(data: Any, privDataType: PrivDataType) {
        when (privDataType) {
            PrivDataType.WEAR_SPO2 -> (getPrivRepository(privDataType) as WearableDataRepository<SpO2>).insert(
                data as SpO2
            )

            PrivDataType.WEAR_BIA -> (getPrivRepository(privDataType) as WearableDataRepository<Bia>).insert(
                data as Bia
            )

            else -> getPrivRepository(privDataType).insertAll(data as MutableList<Nothing>)
        }
        return
    }

    fun deleteFile(privDataType: PrivDataType): Result<Unit> {
        val privRepository = getPrivRepository(privDataType)
        return privRepository.deleteFile()
    }
}
