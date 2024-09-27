package researchstack.domain.usecase.file

import researchstack.domain.repository.FileRepository
import javax.inject.Inject

class GetPresignedUrlUseCase @Inject constructor(private val fileRepository: FileRepository) {
    suspend operator fun invoke(
        fileName: String,
        studyId: String,
    ): Result<String> = fileRepository.getPresignedUrl(fileName, studyId)
}
