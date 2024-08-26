package healthstack.app.sync

import android.content.Context
import android.util.Log
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.common.HEALTH_DATA_FOLDER_NAME
import healthstack.common.model.PrivDataType
import healthstack.healthdata.link.HealthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


class SyncWearDataWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : NetworkAwareWorker(appContext, workerParams) {
    private val healthDataSyncClient: BackendFacade = BackendFacadeHolder.getInstance()

    override suspend fun doTask(): Result {
        val dataDir = "${applicationContext.filesDir}" + HEALTH_DATA_FOLDER_NAME
        val dir = File(dataDir)

        dir.listFiles()?.forEach { file ->
            syncFile(file).onSuccess {
                Log.i(TAG, "mobile->backend ${file.name} success")
                file.delete()
            }
        }
        return Result.success()
    }

    private suspend fun syncFile(file: File): kotlin.Result<Unit> = runCatching {
        Log.i(TAG, "try to sync ${file.name} file to the server")

        val contents = file.readLines()
        syncData(
            PrivDataType.valueOf(contents.first()),
            readCsvStringList(contents.drop(1))
        )
    }

    private fun readCsvStringList(contents: List<String>): List<Map<String, Any>> {
        val schema = contents.first().split('|')

        return contents.drop(1).map {
            schema.zip(it.split('|')).toMap()
        }
    }

    private suspend fun syncData(dataType: PrivDataType, data: List<Map<String, Any>>) {
        Log.i(TAG, "try to sync data. dataType: $dataType, size: ${data.size}")

        FirebaseAuth.getInstance().currentUser?.getIdToken(false)
            ?.addOnSuccessListener { result ->
                result.token?.let { idToken ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            healthDataSyncClient.syncHealthData(
                                "",
                                HealthData(
                                    dataType.name,
                                    data
                                )
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "fail to sync data")
                            e.printStackTrace()
                        }
                    }
                }
            }?.addOnFailureListener {
                Log.e(TAG, "fail to get id token")
            }
    }

    companion object {
        private val TAG = SyncWearDataWorker::class.simpleName
    }
}
