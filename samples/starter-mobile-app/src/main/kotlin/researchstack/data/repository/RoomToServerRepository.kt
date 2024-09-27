package researchstack.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import kotlinx.coroutines.delay
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.pref.SyncTimePref
import researchstack.data.datasource.local.room.dao.TimestampEntityBaseDao
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.log.DataSyncLog
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.usecase.log.AppLogger
import java.io.IOException

abstract class RoomToServerRepository<T : TimestampMapData>(
    dataStore: DataStore<Preferences>,
) {
    protected abstract val timestampEntityBaseDao: TimestampEntityBaseDao<T>
    protected abstract val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>
    protected abstract val syncTimePrefKey: SyncTimePref.SyncTimePrefKey
    private val syncTimePref by lazy { SyncTimePref(dataStore, syncTimePrefKey) }

    protected abstract fun List<T>.toHealthDataModel(): HealthDataModel

    suspend fun sync(studyIds: List<String>): Result<Unit> = runCatching {
        require(studyIds.isNotEmpty())
        var result: Result<Unit> = Result.success(Unit)
        repeat(MAX_RETRY) {
            result = uploadData(studyIds)
            if (result.isSuccess) return@runCatching
            delay(SYNC_DELAY)
        }
        result.getOrThrow()
    }

    private suspend fun uploadData(studyIds: List<String>) = runCatching {
        require(studyIds.isNotEmpty())

        var lastSyncTime = syncTimePref.getLastSyncTime() ?: 0
        val pageSource = timestampEntityBaseDao.getGreaterThan(lastSyncTime)

        var loadResult =
            pageSource.load(PagingSource.LoadParams.Refresh(null, PAGE_LOAD_SIZE, false))

        var nextPage: Int? = -1
        do {
            when (val copiedLoadResult = loadResult) {
                is PagingSource.LoadResult.Page -> {
                    if (copiedLoadResult.data.isEmpty() && nextPage == -1) {
                        AppLogger.saveLog(DataSyncLog("nothing to sync for $syncTimePrefKey"))
                        break
                    }

                    val healthData = copiedLoadResult.data.toHealthDataModel()
                    grpcHealthDataSynchronizer.syncHealthData(studyIds, healthData)
                        .onSuccess {
                            lastSyncTime = copiedLoadResult.data.last().timestamp + 1
                            nextPage = copiedLoadResult.nextKey
                            AppLogger.saveLog(
                                DataSyncLog("sync ${healthData.unifiedDataType}: ${healthData.dataList.size}")
                            )
                        }.onFailure {
                            finishSync(lastSyncTime)
                        }.getOrThrow()
                }

                is PagingSource.LoadResult.Error -> {
                    Log.e(TAG, copiedLoadResult.throwable.stackTraceToString())
                    finishSync(lastSyncTime)
                    throw copiedLoadResult.throwable
                }

                is PagingSource.LoadResult.Invalid -> {
                    AppLogger.saveLog(DataSyncLog("[${syncTimePrefKey.name}] Invalid Page"))
                    Log.e(TAG, "Invalid page")
                    finishSync(lastSyncTime)
                    throw IOException("Invalid page")
                }
            }

            if (nextPage == null) {
                finishSync(lastSyncTime)
            } else {
                // NOTE to reduce server loads
                delay(SYNC_DELAY)
                loadResult = pageSource.load(
                    PagingSource.LoadParams.Append(nextPage!!, PAGE_LOAD_SIZE, false)
                )
            }
        } while (nextPage != null)
    }

    private suspend fun finishSync(lastSyncTime: Long) {
        timestampEntityBaseDao.deleteLEThan(lastSyncTime)
        syncTimePref.update(lastSyncTime)
    }

    companion object {
        private const val PAGE_LOAD_SIZE = 1000
        private const val SYNC_DELAY = 10000L
        private const val MAX_RETRY = 10
        private val TAG = RoomToServerRepository::class.simpleName
    }
}
