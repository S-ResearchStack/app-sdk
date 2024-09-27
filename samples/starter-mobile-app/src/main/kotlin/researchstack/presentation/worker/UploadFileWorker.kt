package researchstack.presentation.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.data.datasource.local.room.dao.FileUploadRequestDao
import researchstack.data.datasource.local.room.entity.FileUploadRequestEntity
import researchstack.domain.model.log.DataSyncLog
import researchstack.domain.usecase.file.DeleteFileUseCase
import researchstack.domain.usecase.file.UploadFileUseCase
import researchstack.domain.usecase.log.AppLogger
import researchstack.presentation.service.ipc.DataUploadRequest
import researchstack.presentation.service.ipc.IPCMessage
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID

@HiltWorker
class UploadFileWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val uploadFileUseCase: UploadFileUseCase,
    private val deleteFileUseCase: DeleteFileUseCase,
    private val fileUploadRequestDao: FileUploadRequestDao,
) : NetworkAwareWorker(appContext, workerParams) {

    private val gson = Gson()
    private fun isSingleUploadFileRequest(): Boolean =
        inputData.hasKeyWithValueOfType<String>(NoteKey)

    override suspend fun doWork(): Result {
        // FIXME don't save FileUploadRequest in  UploadFileWorker
        if (isSingleUploadFileRequest()) {
            enqueueFileUploadRequest()
            return Result.success()
        }

        if (!isAllowedNetwork()) {
            Log.i("NetworkAwareWorker", "skip worker: require wifi network")
            AppLogger.saveLog(DataSyncLog("skip worker: require wifi network"))
            return Result.success()
        }

        return doTask()
    }

    private suspend fun enqueueFileUploadRequest() {
        inputData.getString(NoteKey)?.let { message ->
            val ipcRequest = gson.fromJson(message, IPCMessage::class.java)
            val extras = ipcRequest.extras
            val dataUploadRequest = gson.fromJson(extras, DataUploadRequest::class.java)
            fileUploadRequestDao.insert(
                FileUploadRequestEntity(
                    UUID.randomUUID().toString(),
                    "FileUpload",
                    dataUploadRequest.getStudyId(),
                    mapOf(MessageKey to message)
                )
            )
        }
    }

    override suspend fun doTask(): Result {
        fileUploadRequestDao.findAll().forEach { (jobId, _, studyId, params) ->
            if (!isAllowedNetwork()) return@forEach

            params[MessageKey]?.let {
                handleRequest(it)
                    .onSuccess {
                        fileUploadRequestDao.deleteByJobId(jobId)
                    }.onFailure { exception ->
                        if (exception is FileNotFoundException) {
                            // NOTE duplicated request
                            fileUploadRequestDao.deleteByJobId(jobId)
                        }
                    }
            }
        }
        return Result.success()
    }

    private suspend fun handleSingleUploadRequest(): Result {
        inputData.getString(NoteKey)?.let {
            handleRequest(it)
                .onFailure {
                    enqueueFileUploadRequest()
                }
        }
        return Result.success()
    }

    private suspend fun handleRequest(message: String): kotlin.Result<Unit> {
        // val message = inputData.getString(NoteKey)
        val ipcRequest = gson.fromJson(message, IPCMessage::class.java)
        val extras = ipcRequest.extras
        val dataUploadRequest = gson.fromJson(extras, DataUploadRequest::class.java)
        val filePath = dataUploadRequest.getUploadPath()
        val studyId = dataUploadRequest.getStudyId()
        val fileName = dataUploadRequest.getFileName()
        return withContext(Dispatchers.IO) {
            tryToUploadFile(studyId, fileName, filePath)
                .onSuccess {
                    deleteFileUseCase(fileName)
                    AppLogger.saveLog(DataSyncLog("upload file:$fileName"))
                }.onFailure {
                    AppLogger.saveLog(DataSyncLog("not existed file:$fileName"))
                }
        }
    }

    private suspend fun tryToUploadFile(studyId: String, fileName: String, filePath: String): kotlin.Result<Unit> {
        val file = File(applicationContext.filesDir, fileName)
        if (!file.exists()) return kotlin.Result.failure(FileNotFoundException())

        return uploadFileUseCase(studyId, filePath, file.inputStream())
    }

    companion object {
        internal const val NoteKey = "Note"
        internal const val MessageKey = "message"
    }
}
