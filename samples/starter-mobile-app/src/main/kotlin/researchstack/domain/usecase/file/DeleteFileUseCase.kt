package researchstack.domain.usecase.file

import researchstack.domain.repository.FileRepository
import javax.inject.Inject

class DeleteFileUseCase @Inject constructor(private val fileRepository: FileRepository) {
    suspend operator fun invoke(
        fileName: String
    ): Result<Unit> = fileRepository.deleteFile(fileName)
}
