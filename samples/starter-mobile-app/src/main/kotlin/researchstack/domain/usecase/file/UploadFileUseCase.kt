package researchstack.domain.usecase.file

import android.util.Log
import researchstack.domain.repository.FileRepository
import java.io.InputStream
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(private val fileRepository: FileRepository) {
    suspend operator fun invoke(
        uploadUrl: String,
        fileUrl: String,
    ): Result<Unit> = fileRepository.uploadImage(uploadUrl, fileUrl)

    suspend operator fun invoke(studyId: String, fileName: String, inputStream: InputStream): Result<Unit> =
        fileRepository.getPresignedUrl(fileName, studyId)
            .mapCatching { signedUrl ->
                fileRepository.uploadFile(signedUrl, inputStream).getOrThrow()
            }.onFailure {
                Log.e(UploadFileUseCase::class.simpleName, it.stackTraceToString())
            }
}
