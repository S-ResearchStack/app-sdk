package researchstack.presentation.service.ipc

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import aws.smithy.kotlin.runtime.time.Instant
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import researchstack.domain.model.StudyStatusModel
import researchstack.domain.usecase.file.SaveFileUseCase
import researchstack.domain.usecase.study.GetStudyByIdUseCase
import researchstack.presentation.worker.UploadFileWorker
import researchstack.presentation.worker.UploadFileWorker.Companion.NoteKey
import java.io.FileNotFoundException

internal class MessageIncomingHandler(
    private val context: Context,
    private val scope: CoroutineScope,
    private val getStudyByIdUseCase: GetStudyByIdUseCase,
    private val saveFileUseCase: SaveFileUseCase,
) : Handler(Looper.getMainLooper()) {

    private val gson = Gson()

    override fun handleMessage(msg: Message) {
        Log.i(Tag, "handleMessage ${msg.data.getString(IpcMessageKey)}")
        val requestMessage = msg.data.getString(IpcMessageKey)
        val replyTo = msg.replyTo
        scope.launch {
            kotlin.runCatching {
                val response = requestMessage?.let { data ->
                    handleIpcRequest(data)
                } ?: NoDataMessageError
                replyTo?.let {
                    val bundle = createResponseMessage(response)
                    it.send(bundle)
                    Log.i(Tag, "response ${bundle.data.getString(IpcMessageKey)}")
                }
            }.onFailure {
                Log.i(Tag, "Error[handleMessage]: ${it.stackTraceToString()}")
            }
        }
    }

    @VisibleForTesting
    suspend fun handleIpcRequest(message: String): IPCMessage =
        kotlin.runCatching { gson.fromJson(message, IPCMessage::class.java) }
            .mapCatching { ipcRequest ->
                when (ipcRequest.messageName) {
                    IpcDataUploadRequest -> handleDataRequest(ipcRequest)
                    IpcSubjectIdRequest -> handleSubjectIdRequest(ipcRequest)
                    else -> handleEventRequest(ipcRequest)
                }
            }.recoverCatching {
                Log.e(Tag, it.stackTraceToString())
                InvalidMessageError
            }.getOrThrow()

    private fun handleEventRequest(ipcMessage: IPCMessage): IPCMessage = createIPCMessageResponse(ipcMessage.messageName)

    private fun createIPCMessageResponse(messageName: String, extras: String? = null): IPCMessage =
        IPCMessage(Instant.now().epochSeconds, messageName, extras)

    private suspend fun handleSubjectIdRequest(ipcMessage: IPCMessage): IPCMessage {
        val extras = ipcMessage.extras ?: return InvalidMessageError
        val subjectIdRequest = gson.fromJson(extras, SubjectIdRequest::class.java)
        val study = runCatching {
            getStudyByIdUseCase(subjectIdRequest.studyId).firstOrNull()
        }.getOrNull()

        val subjectIdResponse = if (study?.status == StudyStatusModel.STUDY_STATUS_PARTICIPATING) {
            val successOrFail = if (study.registrationId != null) Success else Fail

            SubjectIdResponse(
                study.id,
                study.registrationId ?: NotParticipated,
                successOrFail
            )
        } else {
            SubjectIdResponse(
                subjectIdRequest.studyId,
                NotParticipated,
                Fail
            )
        }

        return createIPCMessageResponse(IpcSubjectIdResponse, gson.toJson(subjectIdResponse))
    }

    private suspend fun handleDataRequest(ipcMessage: IPCMessage): IPCMessage {
        val extras = ipcMessage.extras ?: return InvalidMessageError
        val dataUploadRequest = gson.fromJson(extras, DataUploadRequest::class.java)
        return saveFile(dataUploadRequest)
            .onSuccess { uploadFileByWorker(ipcMessage, dataUploadRequest.getFileName()) }
            .getOrElse {
                createIPCMessageResponse(
                    IpcDataUploadResponse,
                    gson.toJson(
                        DataUploadResponse(
                            dataUploadRequest.sessionId,
                            dataUploadRequest.uri,
                            Fail,
                            it.message
                        )
                    )
                )
            }
    }

    suspend fun saveFile(request: DataUploadRequest): Result<IPCMessage> =
        context.contentResolver.openInputStream(Uri.parse(request.uri))?.use { inputStream ->
            kotlin.runCatching {
                val fileName = request.getFileName()
                saveFileUseCase(fileName, inputStream).map {
                    createIPCMessageResponse(
                        IpcDataUploadResponse,
                        gson.toJson(
                            DataUploadResponse(request.sessionId, request.uri, Success)
                        )
                    )
                }.getOrThrow()
            }
        } ?: Result.failure(FileNotFoundException("fail to open input-stream"))

    private fun createResponseMessage(response: IPCMessage) =
        Message().apply {
            data = Bundle().apply {
                putString(IpcMessageKey, gson.toJson(response))
            }
        }

    private fun uploadFileByWorker(
        ipcMessage: IPCMessage,
        fileName: String,
    ) {
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                fileName,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<UploadFileWorker>()
                    .setInputData(
                        Data.Builder()
                            .putString(NoteKey, gson.toJson(ipcMessage))
                            .build()
                    )
                    .build()
            )
    }

    companion object {
        private const val Tag: String = "IncomingHandler"
        const val IpcMessageKey: String = "message"
        const val IpcDataUploadRequest: String = "REQ_DATA_UPLOAD"
        const val IpcDataUploadResponse: String = "ACK_DATA_UPLOAD"
        const val NotFoundStudy: String = "not found study"
        internal const val Fail = "1"
        internal const val Success = "0"
        internal const val NotParticipated = "-1"
        const val IpcSubjectIdRequest: String = "REQ_SUBJ_NUM"
        const val IpcSubjectIdResponse: String = "ACK_SUBJ_NUM"

        private val NoDataMessageError = InvalidJsonErrorResponse("no message")
        private val InvalidMessageError = InvalidJsonErrorResponse("invalid message")
    }
}
