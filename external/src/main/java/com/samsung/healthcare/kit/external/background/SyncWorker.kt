package com.samsung.healthcare.kit.external.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.samsung.healthcare.kit.external.background.SyncManager.Companion.HEALTH_DATA_TYPE_KEY
import com.samsung.healthcare.kit.external.data.HealthData
import com.samsung.healthcare.kit.external.data.HealthData.Companion.END_TIME_KEY
import com.samsung.healthcare.kit.external.datastore.MetaDataStore
import com.samsung.healthcare.kit.external.network.ResearchPlatformAdapter
import com.samsung.healthcare.kit.external.source.HealthPlatformAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val metaDataStore: MetaDataStore,
) : CoroutineWorker(context, params) {
    private val healthDataSyncClient: HealthDataSyncClient = ResearchPlatformAdapter.getInstance()
    private val healthPlatformAdapter: HealthPlatformAdapter = HealthPlatformAdapter.getInstance()

    override suspend fun doWork(): Result {
        val healthDataTypeString: String =
            inputData.getString(HEALTH_DATA_TYPE_KEY) ?: return Result.failure()

        val startTime: String = metaDataStore.readLatestSyncTime(healthDataTypeString)
        val endTime: String = Instant.now().toString()

        val healthDataToSync = healthPlatformAdapter
            .getHealthData(startTime, endTime, healthDataTypeString)
            .let { healthData ->
                HealthData(
                    healthData.type,
                    healthData.data.filter {
                        !it.containsKey(END_TIME_KEY) ||
                            Instant.parse(endTime) >= Instant.parse(it[END_TIME_KEY] as String)
                    }
                )
            }

        if (healthDataToSync.data.isNotEmpty()) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)
                ?.addOnSuccessListener { result ->
                    result.token?.let { idToken ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                sync(idToken, healthDataToSync)

                                val latestSyncTime = if (HealthPlatformAdapter.isInterval(healthDataTypeString))
                                    healthDataToSync.data.last()[END_TIME_KEY].toString()
                                else
                                    endTime

                                metaDataStore.saveLatestSyncTime(healthDataTypeString, latestSyncTime)
                            } catch (e: Exception) {
                                Log.d(SyncWorker::class.simpleName, "fail to sync health data")
                                e.printStackTrace()
                            }
                        }
                    }
                }?.addOnFailureListener {
                    Log.d(SyncWorker::class.simpleName, "fail to get id token")
                }
        }

        return Result.success()
    }

    private suspend fun sync(idToken: String, healthDataToSync: HealthData) {
        healthDataSyncClient.sync(idToken, healthDataToSync)
    }
}
