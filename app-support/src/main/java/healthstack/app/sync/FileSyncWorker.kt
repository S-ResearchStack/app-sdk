package healthstack.app.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import healthstack.app.pref.SettingPreference
import healthstack.app.task.repository.TaskRepository
import healthstack.app.task.repository.TaskRepositoryImpl
import healthstack.backend.integration.BackendFacade
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.kit.annotation.ForVerificationGenerated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime

@ForVerificationGenerated
class FileSyncWorker constructor(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    private val fileSyncClient: BackendFacade = BackendFacadeHolder.getInstance()
    private val roomClient: TaskRepository = TaskRepositoryImpl()

    override suspend fun doWork(): Result {
        val settingPreference = SettingPreference(applicationContext)
        val lastSyncTime = LocalDateTime.parse(settingPreference.taskResultSyncTime.first())
        val curTime = LocalDateTime.now()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        auth.currentUser?.getIdToken(false)
            ?.addOnSuccessListener { result ->
                result.token?.let { idToken ->
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            roomClient.getCompletedTasksToSync(lastSyncTime, curTime)
                                .first()
                                .filter {
                                    JSONObject(it.result!![0].response).has("recording")
                                }
                                .map {
                                    val sourcePath: String =
                                        JSONObject(it.result!![0].response).get("recording") as String
                                    val targetPath: String =
                                        "tasks/${it.taskId}/results/${auth.uid}/audio/${sourcePath.split("/").last()}"
                                    fileSyncClient.uploadTaskResultAsFile(
                                        idToken, sourcePath, targetPath
                                    )
                                }
                            settingPreference.setTaskResultSyncTime(curTime.toString())
                        } catch (e: Exception) {
                            Log.e(FileSyncWorker::class.simpleName, "fail to sync file")
                            e.printStackTrace()
                        }
                    }
                }
            }?.addOnFailureListener {
                Log.e(FileSyncWorker::class.simpleName, "fail to get id token")
            }
        return Result.success()
    }
}
