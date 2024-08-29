package healthstack.wearable.support.data

import android.util.Log
import healthstack.common.model.Ecg
import healthstack.common.model.EcgSet
import healthstack.common.model.PpgGreen
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import com.samsung.android.service.health.tracking.data.ValueKey
import healthstack.wearable.support.PrivDataRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

open class EcgTracker {
    @Volatile
    private var isTracking = false
    private lateinit var dataFlow: Flow<EcgSet>
    private val healthTrackerType = HealthTrackerType.ECG
    private val healthDataTracker: HealthTracker by lazy {
        PrivDataRequester.healthTrackingService.getHealthTracker(healthTrackerType)
    }
    private var sendChannel: SendChannel<List<DataPoint>>? = null

    private fun receiveDataFlow(): Flow<EcgSet> = receiveDataPoints().filter {
        it.isNotEmpty()
    }.map {
        it.toECGSet()
    }

    private fun List<DataPoint>.toECGSet(): EcgSet {
        val ppgGreens = mutableListOf(
            this.first().run { PpgGreen(timestamp, getValue(ValueKey.EcgSet.PPG_GREEN)) }
        )
        if (this.size == 10)
            ppgGreens.add(this[5].run { PpgGreen(timestamp, getValue(ValueKey.EcgSet.PPG_GREEN)) })
        return EcgSet(
            this.map { Ecg(it.timestamp, it.getValue(ValueKey.EcgSet.ECG_MV)) },
            ppgGreens,
            this[0].getValue(ValueKey.EcgSet.LEAD_OFF),
            this[0].getValue(ValueKey.EcgSet.MAX_THRESHOLD_MV),
            this[0].getValue(ValueKey.EcgSet.MIN_THRESHOLD_MV),
            this[0].getValue(ValueKey.EcgSet.SEQUENCE)
        )
    }

    fun startTracking(): Flow<EcgSet> {
        if (!isTracking) {
            isTracking = true
            dataFlow = receiveDataFlow()
        }
        return dataFlow
    }

    private fun receiveDataPoints() = callbackFlow {
        val trackerEventListener: HealthTracker.TrackerEventListener = object :
            HealthTracker.TrackerEventListener {
            override fun onDataReceived(dataPoints: List<DataPoint>) {
                trySend(dataPoints)
            }

            override fun onError(trackerError: HealthTracker.TrackerError) {
                Log.e(this::class.simpleName, trackerError.name)
            }

            override fun onFlushCompleted() {
            }
        }
        sendChannel = this.channel
        healthDataTracker.setEventListener(trackerEventListener)
        awaitClose()
    }.flowOn(Dispatchers.IO)

    suspend fun stopTracking(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            if (isTracking.not()) return@withContext Result.success(Unit)
            sendChannel?.close()
            isTracking = false
            healthDataTracker.unsetEventListener()
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    companion object {
        private val TAG = this::class.simpleName
    }
}
