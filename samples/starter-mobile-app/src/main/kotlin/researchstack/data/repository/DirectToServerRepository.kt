package researchstack.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import researchstack.BuildConfig
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.pref.SyncTimePref
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.log.DataSyncLog
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.model.shealth.HealthTypeEnum
import researchstack.domain.usecase.log.AppLogger
import java.time.Instant

abstract class DirectToServerRepository<T : TimestampMapData> {
    protected abstract val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>
    protected abstract val datastore: DataStore<Preferences>
    protected abstract val syncTimePrefKey: SyncTimePref.SyncTimePrefKey
    protected abstract val dataType: HealthTypeEnum
    private val syncTimePref by lazy { SyncTimePref(datastore, syncTimePrefKey) }

    protected abstract fun getDataFrom(lastSyncTime: Long): List<T>

    suspend fun sync(studyIds: List<String>): Result<Unit> = runCatching {
        val lastSyncTime = syncTimePref.getLastSyncTime() ?: Instant.now().toEpochMilli()
        Log.i(DirectToServerRepository::class.simpleName, "lastSyncTime[$lastSyncTime] for ${dataType.name}")
        val data = getDataFrom(lastSyncTime)
        if (data.isEmpty()) {
            Log.i(DirectToServerRepository::class.simpleName, "nothing to sync for ${dataType.name}")
            AppLogger.saveLog(DataSyncLog("nothing to sync for ${dataType.name}"))

            BuildConfig.VERSION_NAME
            return@runCatching
        }

        val dataModel = HealthDataModel(dataType, data.map { it.toDataMap() })
        grpcHealthDataSynchronizer.syncHealthData(studyIds, dataModel).onSuccess {
            syncTimePref.update(data.last().timestamp + 1)
            AppLogger.saveLog(DataSyncLog("sync ${dataType.name}: ${dataModel.dataList.size}"))
        }.getOrThrow()
    }
}
