package researchstack.data.repository

import android.util.Log
import com.samsung.android.service.health.tracking.HealthTracker
import com.samsung.android.service.health.tracking.data.DataPoint
import com.samsung.android.service.health.tracking.data.HealthTrackerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import researchstack.data.local.file.FileRepository
import researchstack.domain.model.Timestamp
import researchstack.domain.repository.WearableDataRepository
import java.io.File

abstract class PrivRepository<T : Timestamp> : WearableDataRepository<T> {
    protected abstract val fileRepository: FileRepository<T>

    @Volatile
    private var isTracking = false
    private lateinit var dataFlow: Flow<T>
    protected abstract val healthTrackerType: HealthTrackerType
    protected open val healthDataTracker: HealthTracker by lazy {
        PrivDataRequester.healthTrackingService.getHealthTracker(healthTrackerType)
    }
    private var sendChannel: SendChannel<List<DataPoint>>? = null

    protected abstract fun receiveDataFlow(): Flow<T>

    override fun startTracking(): Flow<T> {
        if (!isTracking) {
            isTracking = true
            Log.i(PrivRepository::class.simpleName, "start tracking for $healthTrackerType")
            dataFlow = receiveDataFlow()
        }
        return dataFlow
    }

    protected fun receiveDataPoints() = callbackFlow {
        val trackerEventListener: HealthTracker.TrackerEventListener = object :
            HealthTracker.TrackerEventListener {
            override fun onDataReceived(dataPoints: List<DataPoint>) {
                Log.i(this::class.simpleName, "onDataReceived: $healthTrackerType")
                trySend(dataPoints)
            }

            override fun onError(trackerError: HealthTracker.TrackerError) {
                isTracking = false
                Log.e(this::class.simpleName, trackerError.name)
            }

            override fun onFlushCompleted() {
                Log.i(this::class.simpleName, "Flushed")
            }
        }
        sendChannel = this.channel
        healthDataTracker.setEventListener(trackerEventListener)
        Log.i(TAG, "setEventListener - Tracker type: $healthTrackerType")
        awaitClose()
    }.flowOn(Dispatchers.IO)

    protected fun <T> Flow<List<T>>.flatten(): Flow<T> = flow {
        collect { tList -> tList.forEach { t -> emit(t) } }
    }

    override suspend fun stopTracking(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            if (isTracking.not()) return@withContext Result.success(Unit)
            sendChannel?.close()
            isTracking = false
            healthDataTracker.unsetEventListener()
        }.onSuccess {
            Log.i(TAG, "unsetEventListener - Tracker type: $healthTrackerType")
        }.onFailure {
            Log.e(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    override fun insertAll(data: Collection<T>) = fileRepository.saveAll(data)
    override fun insert(data: T) = fileRepository.saveAll(listOf(data))
    override fun deleteFile(): Result<Unit> = fileRepository.deleteFile()
    override fun getCompletedFiles(): List<File> = fileRepository.getCompletedFiles()

    companion object {
        private val TAG = this::class.simpleName
    }
}
