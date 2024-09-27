package researchstack.presentation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import org.apache.commons.io.input.ReaderInputStream
import researchstack.BuildConfig
import researchstack.HEALTH_DATA_FOLDER_NAME
import researchstack.domain.model.log.DataSyncLog
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import researchstack.domain.usecase.event.SendSignalForUploadingStudyDataFileUseCase
import researchstack.domain.usecase.file.UploadFileUseCase
import researchstack.domain.usecase.log.AppLogger
import researchstack.domain.usecase.wearable.SyncWearableDataUseCase
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

@HiltWorker
class UploadHealthDataFileWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted val workerParams: WorkerParameters,
    private val uploadFileUseCase: UploadFileUseCase,
    private val studyRepository: StudyRepository,
    private val shareAgreementRepository: ShareAgreementRepository,
    private val sendSignalForUploadingStudyDataFileUseCase: SendSignalForUploadingStudyDataFileUseCase,
    private val syncWearableDataUseCase: SyncWearableDataUseCase,
) : NetworkAwareWorker(appContext, workerParams) {
    override suspend fun doTask(): Result {
        val dataDir = "${applicationContext.filesDir}" + HEALTH_DATA_FOLDER_NAME
        val dir = File(dataDir)

        dir.listFiles().filter { !it.name.contains("inCompleted-") }
            .forEach { file ->
                syncFile(file).onSuccess { file.delete() }
            }
        return Result.success()
    }

    private suspend fun syncFile(file: File): kotlin.Result<Unit> = runCatching {
        if (BuildConfig.ENABLE_UPLOAD_WEARABLE_DATA_BY_FILE) {
            var dataType: String
            FileInputStream(file).use {
                val reader = BufferedReader(InputStreamReader(it))
                dataType = reader.readLine()
            }

            studyRepository.getActiveStudies().first().filter {
                shareAgreementRepository.getApprovalShareAgreementWithStudyAndDataType(
                    it.id,
                    dataType
                )
            }.forEach { study ->
                val filepath = "${study.registrationId}/$WEAR_DIR/$dataType"
                val filename = "${study.id}-${study.registrationId}-${file.name}"
                uploadFileUseCase(study.id, "$filepath/$filename", file.inputStream())
                    .onSuccess {
                        sendSignal(study.id, filepath, filename)
                        AppLogger.saveLog(DataSyncLog("Upload file to s3 success: $filename"))
                    }.onFailure { e ->
                        AppLogger.saveLog(DataSyncLog("Upload file to s3 fail: $filename ${e.message}"))
                    }.getOrThrow()
            }
        } else {
            syncHealthDataToServer(studyRepository.getActiveStudies().first().map { it.id }, file)
        }
    }

    private suspend fun syncHealthDataToServer(studyIds: List<String>, file: File) {
        FileInputStream(file).use { inputStream ->
            val reader = BufferedReader(InputStreamReader(inputStream))
            val dataType = PrivDataType.valueOf(reader.readLine())
            syncWearableDataUseCase(studyIds, dataType, ReaderInputStream(reader))
        }
    }

    private suspend fun sendSignal(studyId: String, filePath: String, fileName: String) =
        sendSignalForUploadingStudyDataFileUseCase(studyId, filePath, fileName)

    companion object {
        const val WEAR_DIR = "wear"
    }
}
