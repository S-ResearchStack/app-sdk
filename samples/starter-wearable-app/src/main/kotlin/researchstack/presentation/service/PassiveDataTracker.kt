package researchstack.presentation.service

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.BuildConfig
import researchstack.domain.model.Timestamp
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.WearableDataRepository
import java.util.Collections
import javax.inject.Inject

class PassiveDataTracker @Inject constructor(
    private val accMeterRepository: WearableDataRepository<Accelerometer>,
    private val ppgGreenRepository: WearableDataRepository<PpgGreen>,
    private val heartRateRepository: WearableDataRepository<HeartRate>,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val accelerometers = Collections.synchronizedList(ArrayList<Accelerometer>())
    private val ppgGreens = Collections.synchronizedList(mutableListOf<PpgGreen>())
    private val heartRate = Collections.synchronizedList(mutableListOf<HeartRate>())
    private var isAccelerometerConnected = false
    private var isPpgGreensConnected = false
    private var isHeartRateConnected = false

    suspend fun trackingAndSaveWearableData(privDataTypes: List<PrivDataType>) {
        privDataTypes.forEach {
            coroutineScope.launch { it.trackingAndSaveWearableData() }
        }

        PrivDataType.values().filter { it.isPassive && privDataTypes.contains(it).not() }.forEach {
            coroutineScope.launch { it.stopTracking() }
        }
    }

    suspend fun stopTracking() {
        PrivDataType.values().filter { it.isPassive }.forEach {
            coroutineScope.launch {
                it.stopTracking()
                it.insertAll()
            }
        }
    }

    private fun PrivDataType.isConnecting(): Boolean = when (this) {
        PrivDataType.WEAR_ACCELEROMETER -> isAccelerometerConnected
        PrivDataType.WEAR_PPG_GREEN -> isPpgGreensConnected
        PrivDataType.WEAR_HEART_RATE -> isHeartRateConnected
        else -> false
    }

    private fun PrivDataType.setConnectingState(isConnecting: Boolean) {
        when (this) {
            PrivDataType.WEAR_ACCELEROMETER -> isAccelerometerConnected = isConnecting
            PrivDataType.WEAR_PPG_GREEN -> isPpgGreensConnected = isConnecting
            PrivDataType.WEAR_HEART_RATE -> isHeartRateConnected = isConnecting
            else -> this.handleUnsupportedType()
        }
    }

    private suspend fun PrivDataType.trackingAndSaveWearableData() = runCatching {
        if (isConnecting()) return@runCatching
        this.setConnectingState(true)

        val insertInterval = BuildConfig.PASSIVE_DATA_INSERT_INTERVAL_IN_SECONDS

        when (this) {
            PrivDataType.WEAR_ACCELEROMETER -> accMeterRepository.startTracking().collect {
                accelerometers.add(it)
                if (accelerometers.size >= insertInterval * TRACKER_HZ) this.insertAll()
            }

            PrivDataType.WEAR_PPG_GREEN -> ppgGreenRepository.startTracking().collect {
                ppgGreens.add(it)
                if (ppgGreens.size >= insertInterval * TRACKER_HZ) this.insertAll()
            }

            PrivDataType.WEAR_HEART_RATE -> heartRateRepository.startTracking().collect {
                heartRate.add(it)
                if (heartRate.size >= insertInterval) this.insertAll()
            }

            else -> this.handleUnsupportedType()
        }
    }.onFailure {
        setConnectingState(false)
        Log.e(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    private fun PrivDataType.insertAll() {
        coroutineScope.launch {
            when (this@insertAll) {
                PrivDataType.WEAR_ACCELEROMETER -> accelerometers.insertAll(accMeterRepository)
                PrivDataType.WEAR_PPG_GREEN -> ppgGreens.insertAll(ppgGreenRepository)
                PrivDataType.WEAR_HEART_RATE -> heartRate.insertAll(heartRateRepository)
                else -> handleUnsupportedType()
            }
        }
    }

    private suspend fun PrivDataType.stopTracking() = runCatching {
        when (this) {
            PrivDataType.WEAR_ACCELEROMETER -> accMeterRepository.stopTracking()
            PrivDataType.WEAR_PPG_GREEN -> ppgGreenRepository.stopTracking()
            PrivDataType.WEAR_HEART_RATE -> heartRateRepository.stopTracking()
            else -> this.handleUnsupportedType()
        }
        setConnectingState(false)
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    private inline fun <reified T : Timestamp> MutableList<T>.insertAll(
        wearableDataRepository: WearableDataRepository<T>,
    ) = runCatching {
        synchronized(this) {
            if (isEmpty()) return@runCatching

            val toInsert = take(size)
            runCatching { wearableDataRepository.insertAll(toInsert) }.onSuccess {
                removeAll(toInsert)
                Log.i(
                    TAG,
                    "${T::class.simpleName} data inserted to database - inserted size: ${toInsert.size}"
                )
            }.getOrThrow()
        }
    }.onFailure {
        Log.e(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    private fun PrivDataType.handleUnsupportedType() {
        if (this.isPassive) Log.e(TAG, "$this type is not yet supported.")
        else Log.e(TAG, "This is Active Data.")
    }

    companion object {
        private val TAG = PassiveDataTracker::class.simpleName
        private const val TRACKER_HZ = 25
    }
}
