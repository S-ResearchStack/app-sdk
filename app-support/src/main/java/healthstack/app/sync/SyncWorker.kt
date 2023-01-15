package healthstack.app.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import healthstack.app.pref.MetaDataStore
import healthstack.app.sync.SyncManager.Companion.HEALTH_DATA_TYPE_KEY
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.healthdata.link.HealthData
import healthstack.healthdata.link.HealthDataLink
import healthstack.healthdata.link.HealthDataLinkHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SyncWorker constructor(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    private val healthDataSyncClient: BackendFacade = BackendFacadeHolder.getInstance()
    private val healthDataLink: HealthDataLink = HealthDataLinkHolder.getInstance()

    override suspend fun doWork(): Result {
        val healthDataTypeString: String =
            inputData.getString(HEALTH_DATA_TYPE_KEY) ?: return Result.failure()

        val metaDataStore = MetaDataStore(applicationContext)
        val originalToken = metaDataStore.readChangesToken(healthDataTypeString)
            ?: healthDataLink.getChangesToken(healthDataTypeString)

        val changes = healthDataLink.getChanges(originalToken, healthDataTypeString)
        val newToken = changes.token
        val healthDataToSync = changes.healthData

        if (healthDataToSync.data.isNotEmpty()) {
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)
                ?.addOnSuccessListener { result ->
                    result.token?.let { idToken ->
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                sync(idToken, healthDataToSync)

                                metaDataStore.saveChangesToken(healthDataTypeString, newToken)
                            } catch (e: Exception) {
                                Log.d(SyncWorker::class.simpleName, "fail to sync health data")
                                e.printStackTrace()
                            }
                        }
                    }
                }?.addOnFailureListener {
                    Log.d(SyncWorker::class.simpleName, "fail to get id token")
                }
        } else {
            metaDataStore.saveChangesToken(healthDataTypeString, newToken)
            Log.d(SyncWorker::class.simpleName, "nothing to sync")
        }

        return Result.success()
    }

    private suspend fun sync(idToken: String, healthDataToSync: HealthData) {
        healthDataSyncClient.sync(idToken, healthDataToSync)
    }
}
